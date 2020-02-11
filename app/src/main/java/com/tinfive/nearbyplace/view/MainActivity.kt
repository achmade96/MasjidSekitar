package com.tinfive.nearbyplace.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.adapter.FasilitasAdapter
import com.tinfive.nearbyplace.adapter.ListMasjidAdapter
import com.tinfive.nearbyplace.adapter.MasjidFasilitasAdapter
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.EndPoint.MY_PERMISSION_CODE
import com.tinfive.nearbyplace.networks.MasjidApi
import com.tinfive.nearbyplace.networks.RetrofitClient
import com.tinfive.nearbyplace.utils.EqualSpacingItemDecoration
import com.tinfive.nearbyplace.utils.MapsUtils
import com.tinfive.nearbyplace.utils.MapsUtils.Companion.getUrl
import com.tinfive.nearbyplace.viewmodel.MapActivityModel
import com.tinfive.nearbyplace.viewmodel.MasjidListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    //fasilitas
    internal lateinit var restApi: MasjidApi //get list fasilitas

    //MAPS
    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    //List Masjid
    lateinit var viewModel: MasjidListViewModel
    private val masjidAdapter =
        ListMasjidAdapter(ArrayList())

    //Search View
    private var searchView: SearchView? = null
    private var myCompositeDisposable = CompositeDisposable()

    private lateinit var mapsModel: MapActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restApi = RetrofitClient.getFasilitasList() //filterasi

        Handler().post { fetchCategories() } //DAPETIN LIST FASILITAS MENGGUNAKAN API / RETROFIT

        recycler_masjids.setHasFixedSize(true)
        recycler_masjids.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[MasjidListViewModel::class.java]
        mapsModel = ViewModelProvider(this).get(MapActivityModel::class.java)

//        initBottom()

        nav.setOnNavigationItemSelectedListener { p0 ->
            when (p0.itemId) {
                R.id.btn_sort -> {
                    println("SORT BELOM AKTIF")
                }
                R.id.btn_filter -> {
                    showFilterDialog()
                }
            }
            true
        }

        observeViewModel() //GET LIST MASJID
        initializeComponent() //LAYOUTING LIST MASJID

        //actionbar
        setSupportActionBar(toolbar)
        //set back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            viewModel.refresh()
        }

        //Request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                accessMapLiveLocation()
            }
        } else {
            accessMapLiveLocation()
        }
        observeViewMapsModel() // ALL ABOUT GMAPS MARKER
    }

    //MELAKUKAN SHOW / POP-UP DIALOG DAN MENAMPILKAN LIST FASILITAS
    private fun showFilterDialog() {

        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("Pilih Fasilitas")

        val inflater = this.layoutInflater
        val filter_options_layout = inflater.inflate(R.layout.dialog_filter, null)

        val recycler_options =
            filter_options_layout.findViewById(R.id.recycler_options) as RecyclerView
        recycler_options.setHasFixedSize(true)
        recycler_options.layoutManager = LinearLayoutManager(this)
        val adapter = FasilitasAdapter(baseContext, RetrofitClient.categories!!)
        recycler_options.adapter = adapter

        alertDialog.setView(filter_options_layout)

        alertDialog.setNegativeButton("CANCEL") { dialogInterface, _ -> dialogInterface.dismiss() }
        alertDialog.setPositiveButton("FILTER") { dialogInterface, _ ->


            fetchFilterCategory(adapter.filterArray)
        }

        val dialog = alertDialog.create()

        alertDialog.show()
    }

    //HASIL FASILITAS YANG TELAH DIPILIH AKAN TAMPIL DISINI
    private fun fetchFilterCategory(filterArray: String) {
        myCompositeDisposable.add(
            restApi.getFilteredMasjid(filterArray) //MANGGIL POSTNYA DISINI
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ filterCategories ->

                    map.isVisible == true
                    map.view?.visibility = View.GONE
                    recycler_masjids.visibility = View.VISIBLE
                    recycler_masjids.adapter = MasjidFasilitasAdapter(
                        baseContext,
                        filterCategories
                    ) // HASIL DILEMPAR KE ADAPTER YANG BERBEDA

                },
                    {
                        Toast.makeText(baseContext, "FASILITAS", Toast.LENGTH_SHORT).show()
                        recycler_masjids.visibility = View.VISIBLE
                    })
        )
    }

    //DAPETIN LIST FASILITAS DARI API
    private fun fetchCategories() {
        myCompositeDisposable.add(
            restApi.fasilitasList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ categories ->
                    RetrofitClient.categories = categories
                },
                    {
                        Toast.makeText(baseContext, "", Toast.LENGTH_SHORT).show()
                    })
        )
    }

    //END FILTERASI

    //VIEW MASJID TAMPILAN 2
    private fun observeViewModel() {
        viewModel.masjid.observe(this, Observer { masjid ->
            masjid?.let {
                recycler_masjids.visibility = View.VISIBLE
                masjidAdapter.updateMasjid(it)
                masjidAdapter.setOnItemClickListener(object :
                    ListMasjidAdapter.OnItemClickListener {
                    override fun onItemSelected(masjides: Masjid) {
                        setOnClickItem(masjides.mosqueId, this@MainActivity)
                    }
                })
            }
        })

        viewModel.masjidLoadError.observe(this, Observer { isError ->
            isError?.let { listError.visibility = if (it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    recycler_masjids.visibility = View.GONE
                }
            }
        })

    }

    private fun initializeComponent() {
        viewModel.refresh()

        recycler_masjids.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                EqualSpacingItemDecoration(
                    12,
                    EqualSpacingItemDecoration.HORIZONTAL
                )
            )
            adapter = masjidAdapter
        }
    }

    private fun setOnClickItem(mosqueId: Int, context: Context) {
        val intent = Intent(context, DetailMasjid::class.java)
        intent.putExtra("key", mosqueId)
        context.startActivity(intent)
    }

    //SEARCH OPTIONS
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        //getting the search view from the menu
        val searchViewItem = menu!!.findItem(R.id.search)
        //getting the search view
        val searchView = searchViewItem.actionView as SearchView

        searchView.setOnSearchClickListener {
            if (map.isVisible == true) {
                recycler_masjids.visibility = View.INVISIBLE
                map.view?.visibility = View.GONE
                changeLayoutView()
            } else {
                recycler_masjids.visibility = View.GONE
                map.view?.visibility = View.GONE
            }
        }

        searchView.setOnCloseListener {
            if (map.isVisible == false) {
                recycler_masjids.visibility = View.GONE
                map.view?.visibility = View.VISIBLE
                changeBackLayoutView()
            }
            observeViewModel()
            false
        }
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = "Cari Masjid Yuk"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                recycler_masjids.visibility = View.GONE
                masjidAdapter.filter.filter(query)
//                println("DATA ACTIVITY $query")
                return false
            }

            override fun onQueryTextSubmit(result: String): Boolean {
                recycler_masjids.visibility = View.VISIBLE
                masjidAdapter.filter.filter(result)
                return false
            }
        })
        return true
    }

    private fun changeBackLayoutView() {
        val params: ViewGroup.LayoutParams = recycler_masjids.layoutParams
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        recycler_masjids.layoutParams = params
    }

    private fun changeLayoutView() {
        val params: ViewGroup.LayoutParams = recycler_masjids.layoutParams
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        recycler_masjids.layoutParams = params
    }
    //END SEARCH OPTIONS

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    private fun observeViewMapsModel() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsModel.masjidsList.observe(this, Observer { masjidListMap ->
            masjidListMap?.let {
                println("markerl $masjidListMap")
//                recycler_masjids.visibility = View.VISIBLE


                for (element in masjidListMap) {
                    val markerOptions = MarkerOptions()
                    val googlePlace = element
                    val lat = googlePlace.mosqueLat.toDouble()
                    val lng = googlePlace.mosqueLng.toDouble()
                    val placeName = googlePlace.mosqueName
                    val latLng = LatLng(lat, lng)
                    println("markern $placeName")

                    markerOptions.position(latLng)
                    markerOptions.title(placeName)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker())
                    //Add marker to map
                    mMap.addMarker(markerOptions)
                }

                //Move Camera
                val latLng = LatLng(latitude, longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                if (masjidListMap.isEmpty()) {
                    listError.text = getString(R.string.masjid_not_found)
                    listError.visibility = View.VISIBLE
                    loadingView.visibility = View.GONE
                    recycler_masjids.visibility = View.GONE
                }
            }
        })

        mapsModel.masjidsListError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        mapsModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    recycler_masjids.visibility = View.GONE
                }
            }
        })
    }

    private fun accessMapLiveLocation() {
        buildLocationRequest()
        buildLocationCallback()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 500000
        locationRequest.fastestInterval = 300000
        locationRequest.smallestDisplacement = 10f
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0.locations.size - 1) //Get last location

                if (mMarker != null) {
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                /*val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("HERE")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                mMarker = mMap.addMarker(markerOptions)*/

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                val cu = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                // Animate Camera
                mMap.animateCamera(cu)

                val url = getUrl(latitude, longitude)
                println("markerr ${getUrl(latitude, longitude)}")

                if (MapsUtils.isOnline(this@MainActivity)) {
                    mapsModel.fetchData(url)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.you_are_offline),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkLocationPermisson(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            return false
        } else
            return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermisson()) {
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )
                            mMap.isMyLocationEnabled = true
                        }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isCompassEnabled = false
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    //Add back button
    override fun onSupportNavigateUp(): Boolean {

        //back button to refresh activity masjid sekitar
        startActivity(Intent(this@MainActivity, MainActivity::class.java))
//        initializeComponent() //LAYOUTING LIST MASJID
//        observeViewModel() //GET LIST MASJID

        return true
    }

    override fun onStop() {
        myCompositeDisposable.clear()
        super.onStop()
    }


}


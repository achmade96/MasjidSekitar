package com.tinfive.nearbyplace.view

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.SortActivity
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.EndPoint.MY_PERMISSION_CODE
import com.tinfive.nearbyplace.utils.EqualSpacingItemDecoration
import com.tinfive.nearbyplace.utils.MapsUtils
import com.tinfive.nearbyplace.utils.MapsUtils.Companion.getUrl
import com.tinfive.nearbyplace.utils.setMargins
import com.tinfive.nearbyplace.viewmodel.ListViewModel
import com.tinfive.nearbyplace.viewmodel.MapActivityModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
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

    lateinit var viewModel: ListViewModel
    private val masjidAdapter = ListMasjidAdapter(ArrayList())

    private var searchView: SearchView? = null
    private var myCompositeDisposable = CompositeDisposable()

    private lateinit var mapsModel: MapActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapsModel = ViewModelProvider(this).get(MapActivityModel::class.java)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        viewModel.refresh()

        initBottom()

        nav.setOnNavigationItemSelectedListener { p0 ->
            when (p0.itemId) {
                R.id.btn_sort -> {
                    supportFragmentManager
                        .beginTransaction()
                    val intent = Intent(this@MainActivity, SortActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_filter -> {
                    slideUpDownBottomSheet()
                }
            }

            true
        }

        observeViewModel()
        initializeComponent()

        //actionbar
        setSupportActionBar(toolbar)
        //set back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            viewModel.refresh()
        }

        //===========View======================
        listError.visibility = View.GONE
        //=====================================================

        //Request runtime permission
        if (Build.VERSION.SDK_INT >= 24) {
            if (checkLocationPermission()) {
                accessMapLiveLocation()
            }
        } else {
            accessMapLiveLocation()
        }

        observeViewMapsModel()

    }

    private fun initBottom() {
        tv_car.setOnClickListener {
            Toast.makeText(this,"Parkir Mobil",Toast.LENGTH_SHORT).show()
        }
        tv_motor.setOnClickListener {
            Toast.makeText(this,"Parkir Motor",Toast.LENGTH_SHORT).show()
        }
        tv_wifi.setOnClickListener {
            Toast.makeText(this,"Free Wifi",Toast.LENGTH_SHORT).show()
        }
        tv_ac.setOnClickListener {
            Toast.makeText(this,"Air Conditioner",Toast.LENGTH_SHORT).show()
        }
        bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }
            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        changeToDown()
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }

                }
            }

        })
    }

    private fun slideUpDownBottomSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            changeToTop()
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            changeToDown()
        }
    }

    private fun changeToDown() {
        setMargins(nav, 5, 0, 5, 20)
    }

    private fun changeToTop() {
        setMargins(nav, 5, 0, 5, 700)
    }

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
                recycler_masjids.visibility = View.VISIBLE


                for (i in 0 until masjidListMap.size) {
                    val markerOptions = MarkerOptions()
                    val googlePlace = masjidListMap.get(i)
                    val lat = googlePlace.geometry!!.location!!.lat
                    val lng = googlePlace.geometry!!.location!!.lng
                    val placeName = googlePlace.name
                    val latLng = LatLng(lat, lng)

//                    if (SphericalUtil.computeDistanceBetween(latLng, markerOptions.getPosition()) < 1000)

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

                if (masjidListMap.size <= 0) {
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

    private fun initializeComponent() {
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
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

    //VIEW MASJID TAMPILAN 2
    private fun observeViewModel() {
        viewModel.masjid.observe(this, Observer { masjid ->
            masjid?.let {
                recycler_masjids.visibility = View.VISIBLE
                masjidAdapter.updateMasjid(it)
                masjidAdapter.setOnItemClickListener(object :
                    ListMasjidAdapter.OnItemClickListener {
                    override fun onItemSelected(masjides: DataMasjid) {
                        setOnClickItem(masjides.mosqueName)
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

    private fun setOnClickItem(mosqueName: String) {
        val intent = Intent(this, SortActivity::class.java)
        intent.putExtra("key", mosqueName)
        startActivity(intent)
    }

    //SEARCH OPTION
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        //getting the search view from the menu
        val searchViewItem = menu!!.findItem(R.id.search)
        //getting the search view
        val searchView = searchViewItem.actionView as SearchView

        searchView.setOnSearchClickListener {
            if (map.isVisible == true) {
                map.view?.visibility = View.GONE
                changeLayoutView()
            } else {
                map.view?.visibility = View.VISIBLE
            }
        }

        searchView.setOnCloseListener {
            if (map.isVisible == false) {
                map.view?.visibility = View.VISIBLE
                changeBackLayoutView()
            }
            observeViewModel()
            false
        }
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = "Cari Masjid Sekitar"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                masjidAdapter.filter.filter(query)
//                println("DATA ACTIVITY $query")
                return false
            }
            override fun onQueryTextSubmit(result: String): Boolean {
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
    //END SEARCH OPTION


    //Add back button
    override fun onBackPressed() {
        if (!searchView!!.isIconified) {
            searchView!!.isIconified = true
            return
        }
    }

    override fun onStop() {
        myCompositeDisposable.clear()
        super.onStop()
    }
}


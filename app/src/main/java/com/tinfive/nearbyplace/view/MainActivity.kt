package com.tinfive.nearbyplace.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tinfive.nearbyplace.FilterActivity
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.SortActivity
import com.tinfive.nearbyplace.model.DataMasjid
import com.tinfive.nearbyplace.networks.EndPoint.MY_PERMISSION_CODE
import com.tinfive.nearbyplace.utils.MapHelper
import com.tinfive.nearbyplace.utils.MapsUtils
import com.tinfive.nearbyplace.utils.MapsUtils.Companion.getUrl
import com.tinfive.nearbyplace.utils.MapsUtils.Companion.getZoomLevel
import com.tinfive.nearbyplace.viewmodel.ListViewModel
import com.tinfive.nearbyplace.viewmodel.MapActivityModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    //Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var viewModel: ListViewModel
    private val masjidAdapter = ListMasjidAdapter(ArrayList())

    private lateinit var mapsModel: MapActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsModel = ViewModelProvider(this).get(MapActivityModel::class.java)

        val bottomNavigation: BottomNavigationView = this.findViewById(R.id.nav)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.btn_filter -> {
                    supportFragmentManager
                        .beginTransaction()
                    val intent = Intent(this, FilterActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_sort -> {
                    supportFragmentManager
                        .beginTransaction()
                    val intent = Intent(this, SortActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        //actionbar
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)


        initializeComponent()
        observeViewModel()

        //===========View======================
        listError.visibility = View.GONE
        recycler_masjids.setHasFixedSize(true)
        recycler_masjids.layoutManager = LinearLayoutManager(this)
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
        mapsModel.masjidsList.observe(this, Observer { masjidListOnMap ->
            masjidListOnMap?.let {
                recycler_masjids.visibility = View.VISIBLE



                for (i in masjidListOnMap.indices) {
                    val markerOptions = MarkerOptions()
                    val googlePlace = masjidListOnMap[i]
                    val lat = googlePlace.geometry!!.location!!.lat
                    val lng = googlePlace.geometry!!.location!!.lng
                    val placeName = googlePlace.name
                    val latLng = LatLng(lat, lng)

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


                if (masjidListOnMap.isEmpty()) {
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
            layoutManager = LinearLayoutManager(context)
            adapter = masjidAdapter
        }

        //Requeest runtime Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermisson()) {
                buildLocationRequest()
                buildLocationCallback()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
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
                mLastLocation = p0!!.locations[p0.locations.size - 1] //Get last location

                if (mMarker != null) {
                    mMarker!!.remove()
                }

                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("HERE")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))



                mMarker = mMap.addMarker(markerOptions)

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                val cu = CameraUpdateFactory.newLatLngZoom(latLng, 16f)
                // Animate Camera
                mMap.animateCamera(cu)
                mMap.addCircle(CircleOptions()
                    .strokeColor(Color.argb(255, 56, 167, 252))
                    .strokeWidth(1f).radius(350.0)
                    .center(latLng))

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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermisson()) {
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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
                    override fun onItemSelected(countries: DataMasjid) {

//                        println("PANGGIL MAP ASYU $countries.lat, ${countries.long} ")
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

    //Add back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}


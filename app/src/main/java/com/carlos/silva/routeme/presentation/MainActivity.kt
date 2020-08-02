package com.carlos.silva.routeme.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carlos.silva.routeme.R
import com.carlos.silva.routeme.framework.RouteMeViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.DirectionsApi
import com.google.maps.model.DirectionsResult
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mMainViewModel = ViewModelProviders.of(this, RouteMeViewModelFactory).get(
            MainViewModel::class.java
        )

        mMainViewModel.placeLiveData.observe(this, Observer {
            showRoute(it)
        })

        fab.setOnClickListener(this)

        setupMap()
        setupLastLocation()
    }

    private fun setupMap() {
        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync {
            mMap = it
            if (checkPermission())
                mMap.isMyLocationEnabled = true
            else
                requestPermissions()
        }
    }

    private fun setupLastLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermission()) {
            mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                val latlng = LatLng(it.latitude, it.longitude)
                mMainViewModel.myLocationLiveData.value = latlng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16f))
            }
        }else {
            requestPermissions()
        }
    }

    private fun showRoute(place: Place) {
        mMap.clear()

        val origin = mMainViewModel.myLocationLiveData.value
        val latLngBounds = LatLngBounds.builder()
        origin?.let { o ->
            val result = mMainViewModel.getDirections<DirectionsResult>(
                o.latitude,
                o.longitude,
                place.latLng!!.latitude,
                place.latLng!!.longitude
            )

            val routeLine = PolylineOptions()
                .color(Color.BLUE)
                .width(10f)

            result.routes.forEach { route ->
                route.legs.forEach { leg ->
                    val startLatLng = LatLng(leg.startLocation.lat, leg.startLocation.lng)
                    val endLatLng = LatLng(leg.endLocation.lat, leg.endLocation.lng)
                    val startMarker = MarkerOptions()
                        .title(leg.startAddress)
                        .position(startLatLng)

                    val endMarker = MarkerOptions()
                        .title(leg.endAddress)
                        .position(endLatLng)

                    latLngBounds.include(startLatLng)
                    latLngBounds.include(endLatLng)

                    mMap.addMarker(startMarker)
                    mMap.addMarker(endMarker)

                    leg?.steps?.forEach { step ->
                        routeLine.add(
                            LatLng(step.startLocation.lat, step.startLocation.lng),
                            LatLng(step.endLocation.lat, step.endLocation.lng)
                        )

//                        step?.steps?.forEach {
//                            routeLine.add(
//                                LatLng(it.startLocation.lat, it.startLocation.lng),
//                                LatLng(it.endLocation.lat, it.endLocation.lng)
//                            )
//                        }
                    }
                }
            }

            mMap.addPolyline(routeLine)
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 15))
        }
    }

    private fun openSearch() {
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )

        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        ).setCountry("BR")
            .build(this)
        startActivityForResult(intent, 50)
    }

    private fun checkPermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() = ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        60
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 50) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    mMainViewModel.placeLiveData.value = place
                }
                AutocompleteActivity.RESULT_ERROR -> {

                }
                Activity.RESULT_CANCELED -> {

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 60) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mMap.isMyLocationEnabled = true
                setupLastLocation()
            }
        }
    }

    override fun onClick(v: View?) {
        openSearch()
    }

}
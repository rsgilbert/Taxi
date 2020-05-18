package com.lokech.taxi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import org.jetbrains.anko.support.v4.longToast
import timber.log.Timber
import java.util.*


open class MapFragment : Fragment(), OnMapReadyCallback {
    var map: GoogleMap? = null
    private var mIsRestore = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayout(), container, false)
        mIsRestore = savedInstanceState != null
        setupMap()
        initializePlaces()
        moreOnCreateView(view)
        return view

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.let {
            if (isLocationPermissionsEnabled()) {
                it.isMyLocationEnabled = true
                it.uiSettings.isMyLocationButtonEnabled = true
                moveCameraToCurrentLocation()
            } else {
                requestLocationPermissions()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults.first() != PackageManager.PERMISSION_GRANTED) {
                    longToast("Unable to show current location, permission required")
                } else {
                    setupMap()
                }
            }
        }
    }

    open fun getLayout() = R.layout.fragment_map

    open fun moreOnCreateView(view: View) {}
}

fun MapFragment.initializePlaces() {
    Places.initialize(context!!, getString(R.string.google_maps_key), Locale.US)
}


//private fun showPolygon(isRestore: Boolean) {
//    toast("Show Polygon")
//    val decodedPath =
//        PolyUtil.decode(LINE)
//
//    map?.addPolyline(PolylineOptions().addAll(decodedPath))
//
//
//}

fun MapFragment.setCamera(latLng: LatLng) {
    getMap()?.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            latLng,
            zoom
        )
    )
}

fun MapFragment.setMarker(latLng: LatLng) {
    getMap()?.addMarker(
        MarkerOptions().position(
            latLng
        ).draggable(true)
    )
}


fun MapFragment.getFusedLocationProviderClient(): FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(requireActivity())

fun MapFragment.getMap() = map

fun MapFragment.moveCameraToCurrentLocation() {
    try {
        if (isLocationPermissionsEnabled()) {
            val locationResult: Task<Location> =
                getFusedLocationProviderClient().lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    val location: Location? = task.result
                    location?.let {
                        val currentLatLng = LatLng(
                            it.latitude, it.longitude
                        )
                        val update = CameraUpdateFactory.newLatLngZoom(
                            currentLatLng, zoom
                        )
                        getMap()?.animateCamera(update)
                    }
                }
            }
        }
    } catch (e: SecurityException) {
        Timber.e("Exception: %s", e.message)
    }
}


fun MapFragment.setupMap() =
    (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!.getMapAsync(this)

fun MapFragment.isLocationPermissionsEnabled() =
    ContextCompat.checkSelfPermission(
        context!!, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

fun MapFragment.requestLocationPermissions() =
    ActivityCompat.requestPermissions(
        requireActivity(),
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        LOCATION_REQUEST_CODE
    )

// Constants
const val zoom = 20f
const val LOCATION_REQUEST_CODE = 101


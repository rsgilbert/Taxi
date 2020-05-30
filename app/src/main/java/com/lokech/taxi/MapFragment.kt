package com.lokech.taxi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.maps.android.PolyUtil
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
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
//        setInfoWindow()
        map?.let {
            if (isLocationPermissionsEnabled()) {
                it.isMyLocationEnabled = true
                it.uiSettings.isMyLocationButtonEnabled = true
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


fun MapFragment.addPolyline(line: String) {
    val decodedPath: List<LatLng> = PolyUtil.decode(line)
    getMap()?.addPolyline(PolylineOptions().addAll(decodedPath))
}

fun MapFragment.setCamera(latLng: LatLng) {
    getMap()?.moveCamera(
        CameraUpdateFactory.newLatLngZoom(
            latLng, zoom
        )
    )
}

fun MapFragment.setCameraAroundBounds(
    neBoundLatitude: Double, neBoundLongitude: Double,
    swBoundLatitude: Double, swBoundLongitude: Double
) {
    val neBoundLatLng = LatLng(neBoundLatitude, neBoundLongitude)
    val swBoundLatLng = LatLng(swBoundLatitude, swBoundLongitude)
    val latLngBounds = LatLngBounds(swBoundLatLng, neBoundLatLng)
    getMap()?.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, padding))
}


fun MapFragment.clearMarkers() {
    getMap()?.clear()
}

fun MapFragment.addGreenMarker(latLng: LatLng, snippet: String) {
    val title = "Start"
    getMap()?.apply {
        addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(latLng)
                .title(title)
                .snippet(snippet)

        )
//            .showInfoWindow()
    }
}

fun MapFragment.addRedMarker(latLng: LatLng, snippet: String) {
    val title = "End"
    getMap()?.apply {
        addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .position(latLng)
                .title(title)
                .snippet(snippet)
        )
    }
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
                        setCamera(currentLatLng)
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

fun MapFragment.setInfoWindow() {
    getMap()?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
        override fun getInfoContents(marker: Marker?): View {
            toast("Setting info")
            val infoWindow: View = layoutInflater.inflate(R.layout.info_window, null)
            val titleTextView: TextView = infoWindow.findViewById(R.id.titleTextView)
            titleTextView.text = marker?.title
            val snippetTextView: TextView = infoWindow.findViewById(R.id.snippetTextView)
            snippetTextView.text = marker?.snippet

            return infoWindow
        }

        // Return null here, so that getInfoContents() is called next.
        override fun getInfoWindow(arg0: Marker?): View? {
            return null
        }
    })
}

// Constants
const val zoom = 20F
const val LOCATION_REQUEST_CODE = 101
const val padding = 50


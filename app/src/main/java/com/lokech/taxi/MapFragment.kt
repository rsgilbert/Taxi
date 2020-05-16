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
import com.google.android.gms.tasks.Task
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


open class MapFragment : Fragment(), OnMapReadyCallback {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayout(), container, false)
        mIsRestore = savedInstanceState != null
        setupMap()
        setupFusedLocationProviderClient()
        return view

    }

    open fun getLayout() = R.layout.fragment_map

    fun getMap() = map

    override fun onMapReady(googleMap: GoogleMap?) {
        toast("OnMapReady called")
        map = googleMap
        map?.let {
            if (isLocationPermissionEnabled()) {
                it.isMyLocationEnabled = true
                it.uiSettings.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                requestLocationPermissions()
            }
        }
    }

    fun isLocationPermissionEnabled() =
        ContextCompat.checkSelfPermission(
            context!!, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun setupFusedLocationProviderClient() {
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    open fun getDeviceLocation() {
        try {
            if (isLocationPermissionEnabled()) {
                val locationResult: Task<Location> =
                    mFusedLocationProviderClient.lastLocation
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
                            getMap()?.moveCamera(update)
                        }

                    }
                }
            }
        } catch (e: SecurityException) {
            Timber.e("Exception: %s", e.message)
        }
    }

    private fun requestLocationPermissions() =
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )

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

    open fun setupMap() {
        toast("Setting up map")
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!.getMapAsync(this)
    }

    companion object {
        private var map: GoogleMap? = null
        private var mIsRestore = false
        private const val LOCATION_REQUEST_CODE = 101
        private const val zoom = 14f
        private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    }
}

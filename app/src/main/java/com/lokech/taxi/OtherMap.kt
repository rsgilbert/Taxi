package com.lokech.taxi//package com.lokech.taxi
//
//
//package com.monstercode.hellomaps
//
//import android.content.DialogInterface
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.FrameLayout
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.android.gms.tasks.Task
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
//import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
//import com.google.android.libraries.places.api.net.PlacesClient
//import org.jetbrains.anko.find
//import org.jetbrains.anko.longToast
//import org.jetbrains.anko.toast
//
//class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Retrive location and camera position from saved instance state
//        if (savedInstanceState != null) {
//            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!
//            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_LOCATION)!!
//        }
//
//        setContentView(R.layout.activity_maps)
//
//        // Construct a PlacesClient
//        Places.initialize(applicationContext, getString(R.string.google_maps_key))
//        mPlacesClient = Places.createClient(this)
//
//        // Construct a FusedLocationProviderClient
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//
//        // Build the map
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putParcelable(KEY_CAMERA_LOCATION, mMap.cameraPosition)
//        outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
//        super.onSaveInstanceState(outState)
//    }
//
//    /**
//     * Sets up the options menu.
//     * @param menu The options menu.
//     * @return Boolean.
//     */
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.current_place_menu, menu)
//        return true
//    }
//
//    /**
//     * Handles a click on the menu option to get a place.
//     * @param item The menu item to handle.
//     * @return Boolean.
//     */
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.option_get_place) {
//            showCurrentPlace()
//        }
//        return true
//    }
//
//    override fun onMapReady(p0: GoogleMap?) {
//        mMap = p0!!
//        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
//            override fun getInfoContents(marker: Marker?): View {
//                val map = find<FrameLayout>(R.id.map)
//                var infoWindow = layoutInflater.inflate(
//                    R.layout.info_window, map, false
//                )
//
//                var title = infoWindow.find<TextView>(R.id.title)
//                title.text = marker?.title
//
//                var snippet = infoWindow.find<TextView>(R.id.snippet)
//                snippet.text = marker?.snippet
//
//                return infoWindow
//            }
//
//            // Return null here, so that getInfoContents() is called next.
//            override fun getInfoWindow(arg0: Marker?): View? {
//                return null
//            }
//        })
//        // Prompt the user for permission.
//        getLocationPermission()
//
//        // Turn on the My Location layer and the related control on the map.
//        updateLocationUI()
//
//        // Get the current location of the device and set the position of the map.
//        getDeviceLocation()
//    }
//
//
//    /**
//     * Get current location of the device
//     * Position the map's camera
//     */
//    fun getDeviceLocation() {
//        /**
//         * Get the best and most recent location of the device.
//         * This location may be null in the rare cases when location is not available
//         */
//        try {
//            if (mLocationPermissionGranted) {
//                toast("Pemissions granted. Getting device location")
//                var locationResult: Task<Location> = mFusedLocationProviderClient.lastLocation
//                locationResult.addOnCompleteListener(this, object : OnCompleteListener<Location> {
//                    override fun onComplete(task: Task<Location>) {
//                        toast("Request completed")
//                        if (task.isSuccessful) {
//                            mLastKnownLocation = task.result
//                            if (mLastKnownLocation != null) {
//                                with(mLastKnownLocation!!) {
//                                    toast("Location is $latitude $longitude")
//                                    mMap.moveCamera(
//                                        CameraUpdateFactory.newLatLngZoom(
//                                            LatLng(
//                                                latitude,
//                                                longitude
//                                            ), defaultZoom
//                                        )
//                                    )
//                                }
//                            } else {
//                                Log.d(tag, "Current location is null, using defaults")
//                                Log.e(tag, "Exception: ${task.exception}")
//                                longToast("LOcation is null: ${task.exception}")
//                                mMap.moveCamera(
//                                    CameraUpdateFactory.newLatLngZoom(
//                                        mDefaultLocation, defaultZoom
//                                    )
//                                )
//                                mMap.uiSettings.isMyLocationButtonEnabled = false
//                            }
//                        }
//                    }
//                })
//            }
//        } catch (e: SecurityException) {
//            Log.e(tag, "Exception: ${e.message}")
//        }
//    }
//
//
//    /**
//     * Prompt user for permission to use device location
//     */
//    fun getLocationPermission() {
//        /*
//         * Request location location permission.
//         * The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult
//          */
//        if (ContextCompat.checkSelfPermission(
//                applicationContext,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            mLocationPermissionGranted = true
//        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        mLocationPermissionGranted = false
//        when (requestCode) {
//            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
//                if (grantResults.isNotEmpty() &&
//                    grantResults.first() == PackageManager.PERMISSION_GRANTED
//                ) {
//                    mLocationPermissionGranted = true
//                }
//            }
//        }
//        updateLocationUI()
//    }
//
//    /**
//     * Prompts the user to select the current place from a list of likely places, and shows the
//     * current place on the map - provided the user has granted location permission.
//     */
//    fun showCurrentPlace() {
//        toast("Show currentPlace: Getting current location")
//
//        if (mLocationPermissionGranted) {
//            toast("Permissions granted")
//            // Use fields to define the data types to return
//            var placeFields: List<Place.Field> =
//                listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//
//            // Use the builder to create a FindCurrentPlaceRequest
//            var request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
//
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location
//            var placeResult: Task<FindCurrentPlaceResponse> =
//                mPlacesClient.findCurrentPlace(request)
//            placeResult.addOnCompleteListener(object :
//                OnCompleteListener<FindCurrentPlaceResponse> {
//                override fun onComplete(task: Task<FindCurrentPlaceResponse>) {
//                    if (task.isSuccessful && task.result != null) {
//                        var likelyPlaces: FindCurrentPlaceResponse = task.result!!
//
//                        // Set the count, handle cases where less than 5 entries are returned
//                        var count: Int? = null
//                        if (likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
//                            count = likelyPlaces.placeLikelihoods.size
//                        } else count = M_MAX_ENTRIES
//
//                        var i = 0
//                        likelyPlaces.placeLikelihoods.map {
//                            if (i < 5) {
//                                with(it.place) {
//                                    mLikelyPlaceNames.add(name)
//                                    mLikelyPlaceAddresses.add(address)
//                                    mLikelyPlaceAttributions.add(attributions)
//                                    mLikelyPlaceLatLngs.add(latLng)
//                                }
//                            }
//                            i++
//                        }
//
//                        this@MapsActivity.openPlacesDialog()
//                    } else Log.e(tag, "Exception : ${task.exception}")
//                }
//            })
//        } else {
//            // The user has granted permission
//            Log.i(tag, "The user has granted location permissions")
//            toast("Thank you for granting permissions")
//            mMap.addMarker(
//                MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet))
//            )
//            getLocationPermission()
//        }
//    }
//
//    /**
//     * Display a form allowing the user to select a place from a list of likely places
//     */
//    private fun openPlacesDialog() {
//        // Ask the user to choose the place they are now
//        var listener: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {
//            override fun onClick(dialog: DialogInterface?, which: Int) {
//                // The "which" argument contains the position of the selected item
//                var markerLatLng: LatLng = mLikelyPlaceLatLngs[which]!!
//                var markerSnippet = mLikelyPlaceAddresses[which]!!
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet += "\n" + mLikelyPlaceAttributions[which]
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place
//                mMap.addMarker(
//                    MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet)
//                )
//                // Position the map's camera at the location of the marker
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, defaultZoom))
//            }
//        }
//        var dialog: AlertDialog = AlertDialog.Builder(this)
//            .setTitle(R.string.pick_place)
//            .setItems(mLikelyPlaceNames.toTypedArray(), listener)
//            .show()
//    }
//
//    /**
//     * Update the map's UI settings based on whether the user has granted permissions
//     */
//    private fun updateLocationUI() {
//        try {
//            if(mLocationPermissionGranted) {
//                mMap.isMyLocationEnabled = true
//                mMap.uiSettings.isMyLocationButtonEnabled = true
//            } else {
//                mMap.isMyLocationEnabled = false
//                mMap.uiSettings.isMyLocationButtonEnabled = false
//                mLastKnownLocation = null
//                getLocationPermission()
//            }
//        } catch (e: SecurityException) {
//            Log.e(tag, "Exception: ${e.message}")
//        }
//    }
//
//
//    companion object {
//        private lateinit var mMap: GoogleMap
//        private lateinit var mCameraPosition: CameraPosition
//        private lateinit var mPlacesClient: PlacesClient
//        private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
//        private var mDefaultLocation: LatLng = LatLng(-33.8523341, 151.2106085)
//        private var mLocationPermissionGranted = false
//        private var mLastKnownLocation: Location? = null
//        private lateinit var mLikelyPlaceNames: MutableList<String?>
//        private lateinit var mLikelyPlaceAddresses: MutableList<String?>
//        private lateinit var mLikelyPlaceAttributions: MutableList<List<*>?>
//        private lateinit var mLikelyPlaceLatLngs: MutableList<LatLng?>
//        private var defaultZoom: Float = 25f
//        private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//        private var KEY_CAMERA_LOCATION = "camera_position"
//        private var KEY_LOCATION = "location"
//        private var M_MAX_ENTRIES = 5
//        private var tag = "MapsActivity"
//    }
//
//}

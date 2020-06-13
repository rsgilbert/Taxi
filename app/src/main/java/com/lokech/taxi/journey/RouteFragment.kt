package com.lokech.taxi.journey

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.*

class RouteFragment : MapFragment() {
    val journeyViewModel: JourneyViewModel by viewModels(
        { requireParentFragment() }) {
        JourneyViewModelFactory(journeyId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRoute()
    }

}

fun RouteFragment.setRoute() {
    journeyViewModel.journey.observe(this) {
        val startLatLng = LatLng(it.startLatitude, it.startLongitude)
        val endLatLng = LatLng(it.endLatitude, it.endLongitude)
        setCameraAroundBounds(
            it.neBoundLatitude,
            it.neBoundLongitude,
            it.swBoundLatitude,
            it.swBoundLongitude
        )
        addGreenMarker(startLatLng, snippet = it.startAddress)
        addRedMarker(endLatLng, snippet = it.endAddress)
        addPolyline(it.line)

    }
}


val RouteFragment.journeyId: String
    get() = JourneyFragmentArgs.fromBundle(requireParentFragment().arguments!!).journeyId



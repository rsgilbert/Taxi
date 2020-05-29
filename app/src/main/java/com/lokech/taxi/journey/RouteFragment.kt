package com.lokech.taxi.journey

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.*
import com.lokech.taxi.util.repository

class RouteFragment : MapFragment() {
    val journeyViewModel: JourneyViewModel by viewModels(
        { requireParentFragment() }) {
        JourneyViewModelFactory(journeyId, repository)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRoute()
    }

}

fun RouteFragment.setRoute() {
    journeyViewModel.journey.observe(this) {
        val startLatLng: LatLng = LatLng(it.startLatitude, it.startLongitude)
        val endLatLng: LatLng = LatLng(it.endLatitude, it.endLongitude)
        setCamera(startLatLng)
        addGreenMarker(startLatLng)
        addRedMarker(endLatLng)
        addPolyline(it.line)

    }
}


val RouteFragment.journeyId: Int
    get() = JourneyFragmentArgs.fromBundle(requireParentFragment().arguments!!).journeyId



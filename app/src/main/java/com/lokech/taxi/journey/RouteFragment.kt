package com.lokech.taxi.journey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lokech.taxi.*
import com.lokech.taxi.util.playAudio

class RouteFragment : MapFragment() {
    val journeyViewModel: JourneyViewModel by viewModels(
        { requireParentFragment() }) {
        JourneyViewModelFactory(journeyId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRoute()
    }

    override fun getLayout(): Int = R.layout.fragment_route

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fabStartAudio: FloatingActionButton = view.findViewById(R.id.fab_start_audio)
        val fabEndAudio: FloatingActionButton = view.findViewById(R.id.fab_end_audio)
        fabStartAudio.setOnClickListener {
            playAudio(journeyViewModel.journey.value!!.startAudioUrl)
        }

        fabEndAudio.setOnClickListener {
            playAudio(journeyViewModel.journey.value!!.endAudioUrl)
        }
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


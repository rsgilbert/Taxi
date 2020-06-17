package com.lokech.taxi.journey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lokech.taxi.*
import com.lokech.taxi.dialogs.AudioPlayerDialog
import org.jetbrains.anko.support.v4.toast

class RouteFragment : MapFragment() {
    val journeyViewModel: JourneyViewModel by viewModels(
        { requireParentFragment() }) {
        JourneyViewModelFactory(journeyId)
    }

    override fun whenMapIsReady() {
        super.whenMapIsReady()
        setRoute()
    }
    override fun getLayout(): Int = R.layout.fragment_route

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fabStartAudio: FloatingActionButton = view.findViewById(R.id.fab_start_audio)
        val fabEndAudio: FloatingActionButton = view.findViewById(R.id.fab_end_audio)
        fabStartAudio.setOnClickListener {
            showAudioPlayerDialog(journeyViewModel.journey.value!!.startAudioUrl)
        }

        fabEndAudio.setOnClickListener {
            showAudioPlayerDialog(journeyViewModel.journey.value!!.endAudioUrl)
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

fun RouteFragment.showAudioPlayerDialog(audioUrl: String) {
    if (audioUrl.isNotBlank()) {
        val audioPlayerDialog = AudioPlayerDialog().apply {
            arguments = Bundle().apply {
                putString("audioUrl", audioUrl)
            }
        }
        audioPlayerDialog.show(childFragmentManager, "audioDialog")
    } else toast("Audio was not recorded")
}

val RouteFragment.journeyId: String
    get() = JourneyFragmentArgs.fromBundle(requireParentFragment().arguments!!).journeyId


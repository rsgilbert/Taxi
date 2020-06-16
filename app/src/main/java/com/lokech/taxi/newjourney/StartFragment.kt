package com.lokech.taxi.newjourney

import android.view.View
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lokech.taxi.R
import com.lokech.taxi.addGreenMarker
import com.lokech.taxi.clearMarkers
import com.lokech.taxi.setCamera
import timber.log.Timber


class StartFragment : NewJourneyMapFragment() {
    override fun observePlace() {
        newJourneyViewModel.startPlace.observe(this) {
            it?.let { place ->
                val latLng = LatLng(place.latitude, place.longitude)
                setCamera(latLng)
                clearMarkers()
                addGreenMarker(latLng = latLng, snippet = place.address)
            }
        }
    }

    override fun setAudioUrl(audioUrl: String) {
        newJourneyViewModel.setStartAudioUrl(audioUrl)
        Timber.i("Journey is ${newJourneyViewModel.newJourneyLiveDate.value}")
    }


    override fun getAudioUrl(): String {
        Timber.i("Journey is ${newJourneyViewModel.newJourneyLiveDate.value}")
        return newJourneyViewModel.newJourneyLiveDate.value!!.startAudioUrl
    }

    override val suggestionClickListener = PlaceSuggestionsAdapter.OnClickListener { place ->
        newJourneyViewModel.setStartPlace(place)
        hideSearchBar()
    }

    override fun getSearchBarResourceId(): Int = R.id.start_search_bar

    override fun observeAudioUrl(fab: FloatingActionButton) {
        newJourneyViewModel.newJourneyLiveDate.observe(this) {
            if (it.startAudioUrl.isNotBlank()) {
                fab.visibility = View.VISIBLE
                hideProgressBar()
            }
        }
    }

}



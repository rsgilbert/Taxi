package com.lokech.taxi.newjourney

import android.view.View
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lokech.taxi.R
import com.lokech.taxi.addRedMarker
import com.lokech.taxi.clearMarkers
import com.lokech.taxi.setCamera
import timber.log.Timber


class EndFragment : NewJourneyMapFragment() {
    override fun observePlace() {
        newJourneyViewModel.endPlace.observe(this) {
            it?.let { place ->
                val latLng = LatLng(place.latitude, place.longitude)
                setCamera(latLng)
                clearMarkers()
                addRedMarker(latLng = latLng, snippet = place.address)
            }
        }
    }

    override fun setAudioUrl(audioUrl: String) {

        Timber.i("Journey is ${newJourneyViewModel.newJourneyLiveDate.value}")
        newJourneyViewModel.setEndAudioUrl(audioUrl)
    }

    override fun getAudioUrl(): String {

        Timber.i("Journey is ${newJourneyViewModel.newJourneyLiveDate.value}")
        return newJourneyViewModel.newJourneyLiveDate.value!!.endAudioUrl
    }

    override val suggestionClickListener = PlaceSuggestionsAdapter.OnClickListener { place ->
        newJourneyViewModel.setEndPlace(place)
        hideSearchBar()
    }

    // Return EndFragment's searchBar res id in activity main
    override fun getSearchBarResourceId(): Int = R.id.end_search_bar

    override fun observeAudioUrl(fab: FloatingActionButton) {
        newJourneyViewModel.newJourneyLiveDate.observe(this) {
            if (it.endAudioUrl.isNotBlank()) {
                fab.visibility = View.VISIBLE
                hideProgressBar()
            }
        }
    }

}



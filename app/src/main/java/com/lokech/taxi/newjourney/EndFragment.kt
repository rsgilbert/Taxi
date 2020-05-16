package com.lokech.taxi.newjourney

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.lokech.taxi.MapFragment
import com.lokech.taxi.R
import com.lokech.taxi.setCamera
import com.lokech.taxi.setMarker
import timber.log.Timber

class EndFragment : MapFragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        autocompleteFragment?.apply {
            setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
            setCountry(UG_CODE)
            setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    place.latLng?.let {
                        setCamera(it)
                        setMarker(it)
                    }
                }

                override fun onError(status: Status) {
                    Timber.i("PlaceSelectionListener error $status")
                }
            })
        }
    }

    override fun getLayout() = R.layout.fragment_end
}

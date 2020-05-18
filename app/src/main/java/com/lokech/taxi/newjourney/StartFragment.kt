package com.lokech.taxi.newjourney

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.viewModels
import br.com.mauker.materialsearchview.MaterialSearchView
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.lokech.taxi.MapFragment
import com.lokech.taxi.R
import com.lokech.taxi.setCamera
import com.lokech.taxi.setMarker
import timber.log.Timber

open class StartFragment : MapFragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    )

    lateinit var searchView: MaterialSearchView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        searchView = activity!!.findViewById(R.id.search_view)

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchView.addSuggestion(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.addSuggestion(query)
                return false
            }
        })

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_journey, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> searchView.openSearch()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
    override fun getLayout() = R.layout.fragment_start
}

// constants
const val UG_CODE = "UG"

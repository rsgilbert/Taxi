package com.lokech.taxi.newjourney

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.*
import com.lokech.taxi.util.repository
import com.mancj.materialsearchbar.MaterialSearchBar

open class EndFragment : MapFragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    ) {
        NewJourneyViewModelFactory(repository)
    }


    lateinit var searchBar: MaterialSearchBar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        moveCameraToCurrentLocation()
        initializeSearchBar()
        observeSuggestions()
        observePlace()
        observeNavigateToJourney()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_journey, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> openSearchBar()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}

fun EndFragment.observePlace() {
    newJourneyViewModel.endPlace.observe(this) {
        it?.let { place ->
            val latLng = LatLng(place.latitude, place.longitude)
            setCamera(latLng)
            clearMarkers()
            addRedMarker(latLng, snippet = place.address)
        }
    }
}

fun EndFragment.observeSuggestions() {
    newJourneyViewModel.suggestions.observe(this) {
        searchBar.updateLastSuggestions(it)
    }
}

fun EndFragment.hideSearchBar() {
    searchBar.visibility = View.GONE
}

fun EndFragment.openSearchBar() {
    searchBar.apply {
        visibility = View.VISIBLE
        openSearch()
    }
}

fun EndFragment.initializeSearchBar() {
    val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val placeSuggestionsAdapter = PlaceSuggestionsAdapter(inflater, suggestionClickListener)
    searchBar = requireActivity().findViewById(R.id.end_search_bar)
    searchBar.run {
        setOnSearchActionListener(searchActionListener)
        addTextChangeListener(textWatcher)
        setCustomSuggestionAdapter(placeSuggestionsAdapter)
        setCardViewElevation(20)
    }
}

fun EndFragment.searchPlaces(query: CharSequence?) {
    if (!query.isNullOrBlank()) newJourneyViewModel.searchPlaces(query.toString())
}

val EndFragment.searchActionListener: MaterialSearchBar.OnSearchActionListener
    get() = object : MaterialSearchBar.OnSearchActionListener {
        override fun onSearchStateChanged(enabled: Boolean) {
            if (!enabled) hideSearchBar()
        }

        override fun onButtonClicked(buttonCode: Int) {}
        override fun onSearchConfirmed(text: CharSequence?) {}
    }

val EndFragment.textWatcher: TextWatcher
    get() = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, end: Int, before: Int, count: Int) {
            searchPlaces(s)
        }

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, end: Int, count: Int, after: Int) {}
    }

val EndFragment.suggestionClickListener: PlaceSuggestionsAdapter.OnClickListener
    get() = PlaceSuggestionsAdapter.OnClickListener { place ->
        newJourneyViewModel.setEndPlace(place)
        hideSearchBar()
    }


fun EndFragment.observeNavigateToJourney() {
    newJourneyViewModel.navigateToJourneyLiveData.observe(this) {
        it?.let {
            val action = NewJourneyFragmentDirections.actionNewJourneyFragmentToJourneyFragment(it)
            findNavController().navigate(action)
            newJourneyViewModel.completeNavigateToJourney()
        }
    }
}
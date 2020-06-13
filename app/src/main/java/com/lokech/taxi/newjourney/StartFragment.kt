package com.lokech.taxi.newjourney

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.*
import com.lokech.taxi.util.repository
import com.mancj.materialsearchbar.MaterialSearchBar

open class StartFragment : MapFragment() {
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

fun StartFragment.observePlace() {
    newJourneyViewModel.startPlace.observe(this) {
        it?.let { place ->
            val latLng = LatLng(place.latitude, place.longitude)
            setCamera(latLng)
            clearMarkers()
            addGreenMarker(latLng, snippet = place.address)
        }
    }
}

fun StartFragment.observeSuggestions() {
    newJourneyViewModel.suggestions.observe(this) {
        searchBar.updateLastSuggestions(it)
    }
}

fun StartFragment.hideSearchBar() {
    searchBar.visibility = View.GONE
}

fun StartFragment.openSearchBar() {
    searchBar.apply {
        visibility = View.VISIBLE
        openSearch()
    }
}

fun StartFragment.initializeSearchBar() {
    val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val placeSuggestionsAdapter = PlaceSuggestionsAdapter(inflater, suggestionClickListener)
    searchBar = requireActivity().findViewById(R.id.start_search_bar)
    searchBar.run {
        setOnSearchActionListener(searchActionListener)
        addTextChangeListener(textWatcher)
        setCustomSuggestionAdapter(placeSuggestionsAdapter)
        setCardViewElevation(20)
    }
}

fun StartFragment.searchPlaces(query: CharSequence?) {
    if (!query.isNullOrBlank()) newJourneyViewModel.searchPlaces(query.toString())
}

val StartFragment.searchActionListener: MaterialSearchBar.OnSearchActionListener
    get() = object : MaterialSearchBar.OnSearchActionListener {
        override fun onSearchStateChanged(enabled: Boolean) {
            if (!enabled) hideSearchBar()
        }

        override fun onButtonClicked(buttonCode: Int) {}
        override fun onSearchConfirmed(text: CharSequence?) {}
    }

val StartFragment.textWatcher: TextWatcher
    get() = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchPlaces(s)
        }

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    }

val StartFragment.suggestionClickListener: PlaceSuggestionsAdapter.OnClickListener
    get() = PlaceSuggestionsAdapter.OnClickListener { place ->
        newJourneyViewModel.setStartPlace(place)
        hideSearchBar()
    }
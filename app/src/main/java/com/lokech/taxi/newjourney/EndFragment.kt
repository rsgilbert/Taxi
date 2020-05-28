package com.lokech.taxi.newjourney

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.MapFragment
import com.lokech.taxi.R
import com.lokech.taxi.setCamera
import com.lokech.taxi.setOneMarker
import com.lokech.taxi.util.getRepository
import com.mancj.materialsearchbar.MaterialSearchBar

open class EndFragment : MapFragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    ) {
        NewJourneyViewModelFactory(getRepository())
    }


    lateinit var searchBar: MaterialSearchBar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        initializeSearchBar()
        observeSuggestions()
        observeLatLng()
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

fun EndFragment.observeLatLng() {
    newJourneyViewModel.endPlace.observe(this) {
        it?.let { place ->
            val latLng = LatLng(place.latitude, place.longitude)
            setCamera(latLng)
            setOneMarker(latLng)
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
    searchBar = requireActivity().findViewById(R.id.search_bar)
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
        newJourneyViewModel.setEndLatLng(place)
        hideSearchBar()
    }
package com.lokech.taxi.newjourney

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.lokech.taxi.MapFragment
import com.lokech.taxi.R
import com.lokech.taxi.setCamera
import com.lokech.taxi.setOneMarker
import com.lokech.taxi.util.getRepository
import com.mancj.materialsearchbar.MaterialSearchBar
import org.jetbrains.anko.support.v4.toast

open class StartFragment : MapFragment() {
    val startFragmentViewModel: StartFragmentViewModel by viewModels {
        StartFragmentViewModelFactory(getRepository())
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

fun StartFragment.observeLatLng() {
    startFragmentViewModel.latLng.observe(this) {
        it?.let { latLng ->
            setCamera(latLng)
            setOneMarker(latLng)
        }
    }
}

fun StartFragment.observeSuggestions() {
    startFragmentViewModel.suggestions.observe(this) {
        toast("observing suggestions $it")
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
    searchBar = requireActivity().findViewById(R.id.search_bar)
    searchBar.run {
        setOnSearchActionListener(searchActionListener)
        addTextChangeListener(textWatcher)
        setCustomSuggestionAdapter(placeSuggestionsAdapter)
        setCardViewElevation(20)
    }
}

fun StartFragment.searchPlaces(query: CharSequence?) {
    if (!query.isNullOrBlank()) startFragmentViewModel.searchPlaces(query.toString())
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
        startFragmentViewModel.setLatLng(place)
        hideSearchBar()
    }
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
        startFragmentViewModel.suggestions.observe(this) {
            searchBar.updateLastSuggestions(it)
        }
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
    val placeSuggestionsAdapter = PlaceSuggestionsAdapter(inflater)
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
        override fun onButtonClicked(buttonCode: Int) {}

        override fun onSearchStateChanged(enabled: Boolean) {
            if (!enabled) hideSearchBar()
        }

        override fun onSearchConfirmed(text: CharSequence?) {
            toast("Confirmed")
        }
    }

val StartFragment.textWatcher: TextWatcher
    get() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchPlaces(s)
        }
    }

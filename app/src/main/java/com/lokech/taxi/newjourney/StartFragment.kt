package com.lokech.taxi.newjourney

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.lokech.taxi.*
import com.lokech.taxi.util.getRepository
import com.mancj.materialsearchbar.MaterialSearchBar
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


open class StartFragment : MapFragment(), MaterialSearchBar.OnSearchActionListener, TextWatcher {
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
            R.id.menu_search -> openSearchBar()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun getLayout() = R.layout.fragment_start

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_BACK -> hideSearchBar()
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {}

    override fun onSearchConfirmed(text: CharSequence?) {
        toast("Confirmed")
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        searchPlaces(s)

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
    searchBar = requireActivity().findViewById(R.id.search_bar)
    searchBar.setOnSearchActionListener(this)
    searchBar.addTextChangeListener(this)
    val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val placeSuggestionsAdapter = PlaceSuggestionsAdapter(inflater)
    searchBar.apply {
        setCustomSuggestionAdapter(placeSuggestionsAdapter)
        setCardViewElevation(20)
    }
}

fun StartFragment.searchPlaces(query: CharSequence?) {
    if (!query.isNullOrBlank()) startFragmentViewModel.searchPlaces(query.toString())
}


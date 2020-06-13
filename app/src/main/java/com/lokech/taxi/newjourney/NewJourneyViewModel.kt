package com.lokech.taxi.newjourney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lokech.taxi.data.Direction
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import com.lokech.taxi.data.Search
import com.lokech.taxi.getNetworkService
import com.lokech.taxi.util.flatLatLng
import com.lokech.taxi.util.performNewSearch
import com.lokech.taxi.util.saveJourney
import com.lokech.taxi.util.searchCollection
import kotlinx.coroutines.launch
import timber.log.Timber

class NewJourneyViewModel : ViewModel() {

    val searchWord = MutableLiveData<String>()

    val navigateToJourneyLiveData = MutableLiveData<String>()

    val suggestions = MutableLiveData<List<Place>>()

    val startPlace = MutableLiveData<Place>()

    val endPlace = MutableLiveData<Place>()


    init {
        setSuggestions()
    }
}

fun NewJourneyViewModel.startNavigateToJourney(id: String) {
    navigateToJourneyLiveData.value = id
}

fun NewJourneyViewModel.completeNavigateToJourney() {
    navigateToJourneyLiveData.value = null
}

fun NewJourneyViewModel.setStartPlace(place: Place) {
    startPlace.value = place
}

fun NewJourneyViewModel.setEndPlace(place: Place) {
    endPlace.value = place
}

fun NewJourneyViewModel.setSearchWord(placeName: String) {
    searchWord.value = placeName
}

fun NewJourneyViewModel.postJourney(charge: Long, vehicle: String, time: Long) {
    startPlace.value?.let { startPlace ->
        endPlace.value?.let { endPlace ->
            viewModelScope.launch {
                val direction: Direction? = try {
                    getNetworkService().requestDirection(
                        origin = flatLatLng(startPlace.latitude, startPlace.longitude),
                        destination = flatLatLng(endPlace.latitude, endPlace.longitude)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                direction?.let {
                    val journey = Journey(
                        startLatitude = direction.startLatitude,
                        startLongitude = direction.startLongitude,
                        startAddress = startPlace.address,
                        endLatitude = direction.endLatitude,
                        endLongitude = direction.endLongitude,
                        endAddress = endPlace.address,
                        neBoundLatitude = direction.bounds.northeast.lat,
                        neBoundLongitude = direction.bounds.northeast.lng,
                        swBoundLatitude = direction.bounds.southwest.lat,
                        swBoundLongitude = direction.bounds.southwest.lng,
                        charge = charge,
                        vehicle = vehicle,
                        time = time,
                        picture = startPlace.icon,
                        line = direction.line,
                        duration = direction.duration,
                        distance = direction.distance
                    )
                    saveJourney(journey)
                    startNavigateToJourney(journey.id)
                }
            }
        }
    }
}

fun NewJourneyViewModel.setSuggestions() {
    searchWord.observeForever { searchWord ->
        Timber.i("New searchword is $searchWord")
        searchCollection
            .document(searchWord)
            .addSnapshotListener { snapshot, e ->
                e?.let {
                    Timber.e("Error listening: $e")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    if (it.exists()) {
                        suggestions.value = it.toObject<Search>()?.places
                        Timber.i("Suggestion is ${suggestions.value}")
                    } else viewModelScope.launch {
                        try {
                            performNewSearch(searchWord)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
            }
    }
}


const val NAVIGATE_TO_JOURNEYS_ACTION = 0

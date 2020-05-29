package com.lokech.taxi.newjourney

import androidx.lifecycle.*
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import kotlinx.coroutines.launch
import timber.log.Timber

class NewJourneyViewModel(private val repository: Repository) : ViewModel() {

    private val searchWord = MutableLiveData<String>()

    val action = MutableLiveData<Int>()

    val navigateToJourneyLiveData = MutableLiveData<Int>()

    fun startNavigateToJourney(id: Int) {
        navigateToJourneyLiveData.value = id
    }

    fun completeNavigateToJourney() {
        navigateToJourneyLiveData.value = null
    }

    val suggestions: LiveData<List<Place>> = searchWord.switchMap {
        liveData<List<Place>> {
            emit(repository.getPlaces(it))
        }
    }

    val startPlace = MutableLiveData<Place>()

    val endPlace = MutableLiveData<Place>()

    fun setStartPlace(place: Place) {
        startPlace.value = place
    }

    fun setEndLatLng(place: Place) {
        endPlace.value = place
    }

    fun searchPlaces(placeName: String) {
        searchWord.value = placeName
        viewModelScope.launch {
            repository.searchPlaces(placeName)
        }
    }

    fun postJourney(charge: Long, vehicle: String, time: Long) {
        startPlace.value?.let { startPlace ->
            endPlace.value?.let { endPlace ->
                viewModelScope.launch {
                    val line: String? = repository.getLine(
                        startLatitude = startPlace.latitude,
                        startLongitude = startPlace.longitude,
                        endLatitude = endPlace.latitude,
                        endLongitude = endPlace.longitude
                    )
                    line?.let {
                        val journey = Journey(
                            startLatitude = startPlace.latitude,
                            startLongitude = startPlace.longitude,
                            startAddress = startPlace.address,
                            endLatitude = endPlace.latitude,
                            endLongitude = endPlace.longitude,
                            endAddress = endPlace.address,
                            charge = charge,
                            vehicle = vehicle,
                            time = time,
                            picture = startPlace.icon,
                            line = line
                        )
                        val postedJourney: Journey? = repository.postJourney(journey)
                        postedJourney?.let {
                            Timber.i("Posted journey is $postedJourney")
                            startNavigateToJourney(it.id)
                        }
                    }
                }
            }
        }
        Timber.i("Posting journey")
    }


    fun actionComplete() {
        action.value = null
    }
}

const val NAVIGATE_TO_JOURNEYS_ACTION = 0
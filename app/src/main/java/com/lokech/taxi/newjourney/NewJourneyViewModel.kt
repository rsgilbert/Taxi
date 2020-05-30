package com.lokech.taxi.newjourney

import androidx.lifecycle.*
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Direction
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class NewJourneyViewModel(private val repository: Repository) : ViewModel() {

    private val searchWord = MutableLiveData<String>()

    private var timer = Timer()
    private val delay: Long = 500L

    val action = MutableLiveData<Int>()

    val navigateToJourneyLiveData = MutableLiveData<Int>()

    private fun startNavigateToJourney(id: Int) {
        navigateToJourneyLiveData.value = id
    }

    fun completeNavigateToJourney() {
        navigateToJourneyLiveData.value = null
    }

    val suggestions: LiveData<List<Place>> = searchWord.switchMap {
        repository.getPlaces(it)
    }

    val startPlace = MutableLiveData<Place>()

    val endPlace = MutableLiveData<Place>()

    fun setStartPlace(place: Place) {
        startPlace.value = place
    }

    fun setEndPlace(place: Place) {
        endPlace.value = place
    }

    fun searchPlaces(placeName: String) {
        searchWord.value = placeName
        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch {
                    repository.searchPlaces(placeName)
                }
            }
        }, delay)
    }

    fun postJourney(charge: Long, vehicle: String, time: Long) {
        startPlace.value?.let { startPlace ->
            endPlace.value?.let { endPlace ->
                viewModelScope.launch {
                    val direction: Direction? = repository.getDirection(
                        startLatitude = startPlace.latitude,
                        startLongitude = startPlace.longitude,
                        endLatitude = endPlace.latitude,
                        endLongitude = endPlace.longitude
                    )
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
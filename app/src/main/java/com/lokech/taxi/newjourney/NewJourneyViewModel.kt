package com.lokech.taxi.newjourney

import androidx.lifecycle.*
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import kotlinx.coroutines.launch
import timber.log.Timber

class NewJourneyViewModel(private val repository: Repository) : ViewModel() {

    private val searchWord = MutableLiveData<String>()

    val suggestions: LiveData<List<Place>> = searchWord.switchMap {
        liveData<List<Place>> { repository.getPlaces(it) }
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
                    picture = startPlace.icon
                )
                viewModelScope.launch {
                    repository.postJourney(journey)
                }
            }
        }
        Timber.i("Posting journey with $vehicle and $time")
    }


}
package com.lokech.taxi.newjourney

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
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

    val startLatLng = MutableLiveData<LatLng>()

    val endLatLng = MutableLiveData<LatLng>()

    fun setStartLatLng(place: Place) {
        startLatLng.value = LatLng(place.latitude, place.longitude)
    }

    fun setEndLatLng(place: Place) {
        endLatLng.value = LatLng(place.latitude, place.longitude)
    }

    fun searchPlaces(placeName: String) {
        searchWord.value = placeName
        viewModelScope.launch {
            repository.searchPlaces(placeName)
        }
    }

    fun postJourney(charge: Long, vehicle: String, time: Long) {
        startLatLng.value?.let { startLatLng ->
            endLatLng.value?.let { endLatLng ->
                val journey = Journey(
                    startLatitude = startLatLng.latitude,
                    startLongitude = startLatLng.longitude,
                    endLatitude = endLatLng.latitude,
                    endLongitude = endLatLng.longitude,
                    charge = charge,
                    vehicle = vehicle,
                    time = time
                )
                viewModelScope.launch {
                    repository.postJourney(journey)
                }
            }
        }
        Timber.i("Posting journey with $vehicle and $time")
    }


}
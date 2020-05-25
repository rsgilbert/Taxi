package com.lokech.taxi.newjourney


import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Place
import kotlinx.coroutines.launch

class EndFragmentViewModel(private val repository: Repository) : ViewModel() {

    private val searchWord = MutableLiveData<String>()

    val suggestions: LiveData<List<Place>> = searchWord.switchMap {
        liveData<List<Place>> { repository.getPlaces(it) }
    }

    val latLng = MutableLiveData<LatLng>()

    fun setLatLng(place: Place) {
        latLng.value = LatLng(place.latitude, place.longitude)
    }

    fun searchPlaces(placeName: String) {
        searchWord.value = placeName
        viewModelScope.launch {
            repository.searchPlaces(placeName)
        }
    }
}

package com.lokech.taxi.newjourney


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Place
import kotlinx.coroutines.launch

class StartFragmentViewModel(private val repository: Repository) : ViewModel() {

    val suggestions = MutableLiveData<List<Place>>()

    val latLng = MutableLiveData<LatLng>()

    fun setLatLng(place: Place) {
        latLng.value = LatLng(place.latitude, place.longitude)
    }

    fun searchPlaces(placeName: String) {
        viewModelScope.launch {
            val places: List<Place>? = repository.searchPlaces(placeName)
            places?.let { suggestions.value = it }
        }
    }
}
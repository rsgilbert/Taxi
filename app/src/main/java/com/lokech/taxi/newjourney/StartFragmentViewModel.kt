package com.lokech.taxi.newjourney


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Place
import kotlinx.coroutines.launch

class StartFragmentViewModel(private val repository: Repository) : ViewModel() {

    val suggestions = MutableLiveData<List<Place>>()


    fun searchPlaces(placeName: String) {
        viewModelScope.launch {
            val places: List<Place>? = repository.searchPlaces(placeName)
            places?.let { suggestions.value = it }
        }
    }
}
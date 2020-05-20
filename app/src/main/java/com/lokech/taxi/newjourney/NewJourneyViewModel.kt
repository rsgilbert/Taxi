package com.lokech.taxi.newjourney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokech.taxi.Repository
import kotlinx.coroutines.launch

class NewJourneyViewModel(private val repository: Repository) : ViewModel() {

    val tell = MutableLiveData<String>()

    val suggestions = MutableLiveData<List<String>>()

    fun insertJourney() {

    }

    fun searchPlaces(placeName: String) {
        viewModelScope.launch {
            val places = repository.searchPlaces(placeName)
            suggestions.value = places?.map { it.name }
        }
    }
}
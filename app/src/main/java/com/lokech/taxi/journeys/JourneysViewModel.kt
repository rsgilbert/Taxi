package com.lokech.taxi.journeys

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Journey

class JourneysViewModel(private val repository: Repository) : ViewModel() {

    val journeys: LiveData<List<Journey>> = repository.allJourneys


}
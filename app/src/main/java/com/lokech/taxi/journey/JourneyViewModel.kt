package com.lokech.taxi.journey

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lokech.taxi.Repository
import com.lokech.taxi.data.Journey

class JourneyViewModel(private val journeyId: Int, private val repository: Repository) :
    ViewModel() {

    val journey: LiveData<Journey> = repository.getJourney(journeyId)


}
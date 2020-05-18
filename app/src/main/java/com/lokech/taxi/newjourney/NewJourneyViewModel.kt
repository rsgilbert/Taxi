package com.lokech.taxi.newjourney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lokech.taxi.Repository

class NewJourneyViewModel(repository: Repository) : ViewModel() {

    val tell = MutableLiveData<String>()

    fun insertJourney() {

    }
}
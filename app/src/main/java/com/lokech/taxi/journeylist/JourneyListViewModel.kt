package com.lokech.taxi.journeylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObjects
import com.lokech.taxi.data.Journey
import com.lokech.taxi.util.descendingJourneyListQuery

class JourneysViewModel : ViewModel() {

    val journeys = MutableLiveData<List<Journey>>()


    init {
        setJourneys()
    }
}

fun JourneysViewModel.setJourneys() {
    descendingJourneyListQuery.addSnapshotListener { snapshot, _ ->
        journeys.value = snapshot?.toObjects()
    }
}
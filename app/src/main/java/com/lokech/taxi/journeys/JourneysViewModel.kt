package com.lokech.taxi.journeys

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObjects
import com.lokech.taxi.data.Journey
import com.lokech.taxi.util.journeyCollection

class JourneysViewModel : ViewModel() {

    val journeys = MutableLiveData<List<Journey>>()


    init {
        setJourneys()
    }
}

fun JourneysViewModel.setJourneys() {
    journeyCollection.addSnapshotListener { snapshot, _ ->
        journeys.value = snapshot?.toObjects()
    }
}
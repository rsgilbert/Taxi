package com.lokech.taxi.journey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.lokech.taxi.data.Journey
import com.lokech.taxi.util.journeyCollection
import timber.log.Timber

class JourneyViewModel(val journeyId: String) : ViewModel() {

    val journey = MutableLiveData<Journey>()

    init {
        setJourney()
    }
}

fun JourneyViewModel.setJourney() {
    journeyCollection.document(journeyId)
        .addSnapshotListener { snapshot, e ->
            e?.let {
                Timber.e("Error listening: $e")
                return@addSnapshotListener
            }
            journey.value = snapshot?.toObject<Journey>()
        }
}
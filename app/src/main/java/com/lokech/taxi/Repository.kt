package com.lokech.taxi

import com.lokech.taxi.data.Dao
import com.lokech.taxi.data.Journey

class Repository(private val dao: Dao) {

    val allJourneys = dao.allJourneys

    fun insertJourney(journey: Journey) = dao.insertOneJourney(journey)
}
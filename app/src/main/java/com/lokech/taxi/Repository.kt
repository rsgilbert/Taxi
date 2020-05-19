package com.lokech.taxi

import com.lokech.taxi.data.Dao
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import com.lokech.taxi.data.PlaceSearchRequest
import timber.log.Timber

class Repository(private val dao: Dao) {

    val allJourneys = dao.allJourneys

    fun insertJourney(journey: Journey) = dao.insertOneJourney(journey)


    suspend fun searchPlaces(placeName: String): List<Place>? {
        return try {
            val searchRequest: PlaceSearchRequest = getNetworkService().searchPlaces(placeName)

            Timber.i("Places are $searchRequest")
            searchRequest.candidates
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e("Error searching places: ${e.message}")
            null
        }
    }
}
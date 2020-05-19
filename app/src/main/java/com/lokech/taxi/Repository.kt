package com.lokech.taxi

import com.lokech.taxi.data.*
import timber.log.Timber

class Repository(private val dao: Dao) {

    val allJourneys = dao.allJourneys

    fun insertJourney(journey: Journey) = dao.insertOneJourney(journey)


    suspend fun searchPlaces(placeName: String): List<Place>? {
        return try {
            val searchResults: PlaceTextSearch = getNetworkService().searchPlaces(placeName)
            Timber.i("Places are ${searchResults.places}")
            searchResults.places

        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e("Error searching places: ${e.message}")
            null
        }
    }
}
package com.lokech.taxi

import com.lokech.taxi.data.*

class Repository(private val dao: Dao) {

    val allJourneys = dao.allJourneys

    suspend fun insertJourney(journey: Journey) = dao.insertOneJourney(journey)

    suspend fun getPlaces(searchWord: String): List<Place> {
        val words = dao.getWords(searchWord)
        val placeIds = words.map { it.placeId }.toTypedArray()
        return dao.getPlaces(placeIds)
    }

    suspend fun searchPlaces(searchWord: String) {
        return try {
            val searchResults: PlaceTextSearch = getNetworkService().searchPlaces(searchWord)
            val places = searchResults.places
            val words = places.map { Word(word = searchWord, placeId = it.id) }
            dao.insertWords(words)
            dao.insertPlaces(places)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
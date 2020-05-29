package com.lokech.taxi

import androidx.lifecycle.LiveData
import com.lokech.taxi.data.*
import com.lokech.taxi.util.flatLatLng
import timber.log.Timber

class Repository(private val dao: Dao) {

    val allJourneys = dao.allJourneys

    fun getJourney(journeyId: Int): LiveData<Journey> {
        return dao.getJourney(journeyId)
    }

    suspend fun getPlaces(searchWord: String): List<Place> {
        val words = dao.getWords(searchWord)
        val placeIds = words.map { it.placeId }.toTypedArray()
        val places = dao.getPlaces(placeIds)
        Timber.i("Places are $places")
        return places
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

    suspend fun getLine(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): String? {
        val origin: String = flatLatLng(startLatitude, startLongitude)
        val destination: String = flatLatLng(endLatitude, endLongitude)
        return try {
            val direction: Direction = getNetworkService().requestDirection(origin, destination)
            direction.line
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun postJourney(journey: Journey): Journey? {
        return try {
            dao.insertOneJourney(journey)
            dao.getOneJourneyByStartAddress(journey.startAddress)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
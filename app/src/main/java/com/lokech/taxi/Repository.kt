package com.lokech.taxi

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.lokech.taxi.data.*
import com.lokech.taxi.util.flatLatLng

class Repository(private val dao: Dao) {

    val allJourneys = dao.allJourneys

    fun getJourney(journeyId: Int): LiveData<Journey> {
        return dao.getJourney(journeyId)
    }

    fun getPlaces(searchWord: String): LiveData<List<Place>> {
        val words = dao.getWords(searchWord)
        val placeIdsLiveData: LiveData<Array<String>> = words.map { wordList ->
            wordList.map { it.placeId }.toTypedArray()
        }
        return placeIdsLiveData.switchMap {
            dao.getPlaces(it)
        }
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


    suspend fun getDirection(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): Direction? {
        val origin: String = flatLatLng(startLatitude, startLongitude)
        val destination: String = flatLatLng(endLatitude, endLongitude)
        return try {
            getNetworkService().requestDirection(origin, destination)
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
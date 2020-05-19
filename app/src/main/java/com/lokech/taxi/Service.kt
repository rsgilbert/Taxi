package com.lokech.taxi

import com.lokech.taxi.data.PlaceTextSearch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val localUrl = "http://10.0.2.2:5000"
private const val baseUrl = "https://taxi-api.herokuapp.com"
private const val inUseUrl = localUrl
private const val placeTextSearchUrl =
    "https://maps.googleapis.com/maps/api/place/textsearch/json?region=UG&key=$googleMapKey"


interface Service {
    @GET(placeTextSearchUrl)
    suspend fun searchPlaces(@Query("query") query: String): PlaceTextSearch


}

/**
 * Main entry point for network access.
 */
private val service: Service by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl(inUseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(Service::class.java)
}

fun getNetworkService() = service

package com.lokech.taxi

import com.lokech.taxi.data.PlaceSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val localUrl = "http://10.0.2.2:5000"
private const val baseUrl = "https://taxi-api.herokuapp.com"
private const val inUseUrl = localUrl
private const val placeSearchUrl =
    "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?inputtype=textquery&fields=photos,formatted_address,name&key=$googleMapKey"
/**
 * A retrofit service to fetch data
 */
interface Service {
    @GET(placeSearchUrl)
    suspend fun searchPlaces(@Query("input") input: String): PlaceSearchRequest



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

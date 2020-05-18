package com.lokech.taxi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val localUrl = "http://10.0.2.2:5000"
private const val baseUrl = "https://taxi-api.herokuapp.com"
private const val inUseUrl = localUrl

/**
 * A retrofit service to fetch data
 */
interface Service {


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

package com.lokech.taxi.data

import java.util.*


data class Journey(
    val id: String = Date().time.toString(),
    val startLatitude: Double = 0.0,
    val startLongitude: Double = 0.0,
    val startAddress: String = "",
    val endLatitude: Double = 0.0,
    val endLongitude: Double = 0.0,
    val endAddress: String = "",
    val neBoundLatitude: Double = 0.0,
    val neBoundLongitude: Double = 0.0,
    val swBoundLatitude: Double = 0.0,
    val swBoundLongitude: Double = 0.0,
    val time: Long = 0,
    val charge: Long = 0,
    val vehicle: String = "",
    val picture: String = "",
    val line: String = "",
    val duration: String = "",
    val distance: String = ""
)
package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Journey(
    val startLatitude: Double,
    val startLongitude: Double,
    val startAddress: String,
    val endLatitude: Double,
    val endLongitude: Double,
    val endAddress: String,
    val neBoundLatitude: Double,
    val neBoundLongitude: Double,
    val swBoundLatitude: Double,
    val swBoundLongitude: Double,
    val time: Long,
    val charge: Long,
    val vehicle: String,
    val picture: String? = null,
    val line: String,
    val duration: String,
    val distance: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
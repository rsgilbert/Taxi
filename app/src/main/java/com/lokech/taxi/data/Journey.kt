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
    val time: Long,
    val charge: Long,
    val vehicle: String,
    val picture: String? = null,
    val line: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
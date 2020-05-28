package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Journey(
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double,
    val endLongitude: Double,
    val time: Long,
    val charge: Long,
    val vehicle: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
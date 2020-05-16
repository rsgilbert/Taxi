package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Journey(
    @PrimaryKey
    val _id: String,
    val startLatitude: String,
    val startLongitude: String,
    val endLatitude: String,
    val charge: Long
)
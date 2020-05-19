package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String,
    val latitude: Double,
    val longitude: Double,
    val address: String
)
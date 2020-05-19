package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    val place_id: String,
    val name: String


)
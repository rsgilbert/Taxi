package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Search(
    @PrimaryKey
    val word: String,
    val placeId: String
)
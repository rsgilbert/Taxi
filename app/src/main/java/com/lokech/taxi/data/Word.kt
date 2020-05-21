package com.lokech.taxi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    val word: String,
    val placeId: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
package com.lokech.taxi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Place(
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String,
    val latitude: Double,
    val longitude: Double,
    val address: String
) : Parcelable
package com.lokech.taxi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @get:Query("SELECT * FROM Journey")
    val allJourneys: LiveData<Journey>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneJourney(journey: Journey)
}
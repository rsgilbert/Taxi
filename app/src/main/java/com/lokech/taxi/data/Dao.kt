package com.lokech.taxi.data

//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query

//@Dao
//interface Dao {
//
//    @get:Query("SELECT * FROM Journey")
//    val allJourneys: LiveData<List<Journey>>
//
//    @Query("SELECT * FROM Journey WHERE id = :journeyId LIMIT 1")
//    fun getJourney(journeyId: Int): LiveData<Journey>
//
//    @Query("SELECT * FROM Word WHERE word = :searchWord")
//    fun getWords(searchWord: String): LiveData<List<Word>>
//
//    @Query("SELECT * FROM Place WHERE id IN (:placeIds)")
//    fun getPlaces(placeIds: Array<String>): LiveData<List<Place>>
//
//    @Query("SELECT * FROM Journey WHERE startAddress = :startAddress LIMIT 1")
//    suspend fun getOneJourneyByStartAddress(startAddress: String): Journey
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOneJourney(journey: Journey)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWord(word: Word)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWords(words: List<Word>)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPlaces(places: List<Place>)
//
//
//}
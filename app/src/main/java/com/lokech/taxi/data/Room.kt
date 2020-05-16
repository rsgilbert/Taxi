package com.lokech.taxi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Journey::class], version = 1)
abstract class TaxiDatabase : RoomDatabase() {
    abstract val dao: Dao
}

private lateinit var INSTANCE: TaxiDatabase

fun getDatabase(context: Context): TaxiDatabase {
    synchronized(TaxiDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TaxiDatabase::class.java,
                "CampusHubDatabase"
            ).build()
        }
    }
    return INSTANCE
}
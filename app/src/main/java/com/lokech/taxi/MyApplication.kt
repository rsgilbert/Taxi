package com.lokech.taxi

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MyApplication : MultiDexApplication() {

    /**
     *  Coroutine scope for the application
     */
    private val applicationScope = CoroutineScope(Dispatchers.Default)


    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        delayedUnit()
    }


    private fun delayedUnit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
        }
    }


}

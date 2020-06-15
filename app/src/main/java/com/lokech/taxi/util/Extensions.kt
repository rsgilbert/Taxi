package com.lokech.taxi.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lokech.taxi.R
import com.lokech.taxi.REQUEST_AUDIO_PERMISSION_CODE
import org.jetbrains.anko.support.v4.toast
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import java.io.IOException

val Fragment.supportActionBar: ActionBar?
    get() =
        (activity as AppCompatActivity).supportActionBar

fun Fragment.setSupportActionBar(toolbar: Toolbar) =
    (activity as AppCompatActivity).setSupportActionBar(toolbar)


val Context.googleMapsKey: String
    get() = getString(R.string.google_maps_key)

val LocalDateTime.millis: Long
    get() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())


val Fragment.writeToStoragePermission: Int
    get() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
val Fragment.recordAudioPermission: Int
    get() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.RECORD_AUDIO
    )
val Fragment.hasRecordingPermissions: Boolean
    get() = recordAudioPermission == PackageManager.PERMISSION_GRANTED

fun Fragment.requestRecordingPermissions() {
    ActivityCompat.requestPermissions(
        requireActivity(),
        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
        REQUEST_AUDIO_PERMISSION_CODE
    )
}


fun Fragment.playAudio(audioUrl: String) {
    MediaPlayer().apply {
        try {
            toast("Audio path is $audioUrl")
            Timber.i("Audio path is $audioUrl")
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                it.start()
            }
        } catch (e: IOException) {
            toast("No audio was recorded")
            e.printStackTrace()
        }
    }
}
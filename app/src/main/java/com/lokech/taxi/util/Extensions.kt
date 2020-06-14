package com.lokech.taxi.util

import android.content.Context
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.lokech.taxi.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

val Fragment.supportActionBar: ActionBar?
    get() =
        (activity as AppCompatActivity).supportActionBar

fun Fragment.setSupportActionBar(toolbar: Toolbar) =
    (activity as AppCompatActivity).setSupportActionBar(toolbar)


val Context.googleMapsKey: String
    get() = getString(R.string.google_maps_key)

val LocalDateTime.millis: Long
    get() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
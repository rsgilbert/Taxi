package com.lokech.taxi.util

import android.content.Context
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.lokech.taxi.R

val Fragment.supportActionBar: ActionBar?
    get() =
        (activity as AppCompatActivity).supportActionBar

fun Fragment.setSupportActionBar(toolbar: Toolbar) =
    (activity as AppCompatActivity).setSupportActionBar(toolbar)


val Context.googleMapsKey: String
    get() = getString(R.string.google_maps_key)

//fun Map<String, Any>.asJourney(): Journey {
//    return Journey(
//        id = get("id") as String,
//        name = get("name") as String,
//        price = get("price") as Long,
//        description = get("description") as String,
//        displayPicture = get("displayPicture") as String,
//        pictures = get("pictures") as List<String>
//    )
//}
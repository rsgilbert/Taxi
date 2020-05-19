package com.lokech.taxi.util

import android.content.Context
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.lokech.taxi.R
import com.lokech.taxi.Repository
import com.lokech.taxi.data.getDatabase

val Fragment.supportActionBar: ActionBar?
    get() =
        (activity as AppCompatActivity).supportActionBar

fun Fragment.setSupportActionBar(toolbar: Toolbar) =
    (activity as AppCompatActivity).setSupportActionBar(toolbar)

fun Fragment.getRepository() = Repository(getDatabase(context!!).dao)

val Context.googleMapsKey: String
    get() = getString(R.string.google_maps_key)

//fun Fragment.observe() {
//    import androidx.lifecycle.observe
//}
package com.lokech.taxi.util

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

val Fragment.supportActionBar: ActionBar?
    get() =
        (activity as AppCompatActivity).supportActionBar

fun Fragment.setSupportActionBar(toolbar: Toolbar) =
    (activity as AppCompatActivity).setSupportActionBar(toolbar)


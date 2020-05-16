package com.lokech.taxi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

open class MapFragment : Fragment(), OnMapReadyCallback {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayout(), container, false)
        mIsRestore = savedInstanceState != null
        setupMap()
        return view

    }

    open fun getLayout() = R.layout.fragment_map

    fun getMap() = map

    override fun onMapReady(googleMap: GoogleMap?) {
        toast("OnMapReady called")
        Timber.i("OnMapReady, map is $googleMap")
        map = googleMap
    }

    private fun setupMap() {
        toast("Setting up map")
        Timber.i("setupMap called")
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!.getMapAsync(this)
    }

    companion object {
        private var map: GoogleMap? = null
        private var mIsRestore = false
    }
}

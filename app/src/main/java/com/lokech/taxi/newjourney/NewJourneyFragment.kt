package com.lokech.taxi.newjourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lokech.taxi.R
import com.lokech.taxi.databinding.FragmentNewJourneyBinding

class NewJourneyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentNewJourneyBinding>(
            inflater,
            R.layout.fragment_new_journey, container, false
        )


        return binding.root

    }
}
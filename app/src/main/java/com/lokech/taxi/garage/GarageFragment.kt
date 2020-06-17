package com.lokech.taxi.garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lokech.taxi.R
import com.lokech.taxi.databinding.FragmentGarageBinding

class GarageFragment : Fragment() {
    val garageViewModel: GarageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentGarageBinding>(
            inflater, R.layout.fragment_garage, container, false
        )

        binding.garageViewModel = garageViewModel
        binding.apply {
            lifecycleOwner = viewLifecycleOwner

        }

        return binding.root

    }
}
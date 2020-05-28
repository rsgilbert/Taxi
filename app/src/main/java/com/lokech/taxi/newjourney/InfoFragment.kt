package com.lokech.taxi.newjourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lokech.taxi.R
import com.lokech.taxi.databinding.FragmentInfoBinding
import com.lokech.taxi.util.getRepository

class InfoFragment : Fragment() {
    private val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    ) {
        NewJourneyViewModelFactory(getRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentInfoBinding>(
            inflater, R.layout.fragment_info, container, false
        )

        binding.apply {
            postButton.setOnClickListener {
                val vehicle = vehicle.text.toString()
                val time = time.text.toString()
                newJourneyViewModel.postJourney(vehicle, time)
            }
        }


        return binding.root

    }
}

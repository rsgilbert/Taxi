package com.lokech.taxi.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lokech.taxi.R
import com.lokech.taxi.databinding.FragmentJourneyInfoBinding
import com.lokech.taxi.util.repository
import org.jetbrains.anko.support.v4.toast

class InfoFragment : Fragment() {
    private val journeyViewModel: JourneyViewModel by viewModels(
        { requireParentFragment() }
    ) {
        JourneyViewModelFactory(journeyId, repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentJourneyInfoBinding>(
            inflater, R.layout.fragment_journey_info, container, false
        )

        binding.journeyViewModel = journeyViewModel
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            bookButton.setOnClickListener {
                toast("Booking")
            }
        }


        return binding.root

    }
}

val InfoFragment.journeyId: Int
    get() = JourneyFragmentArgs.fromBundle(requireParentFragment().arguments!!).journeyId


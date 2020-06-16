package com.lokech.taxi.newjourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.lokech.taxi.R
import com.lokech.taxi.databinding.ViewPagerBinding

class NewJourneyFragment : Fragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ViewPagerBinding>(
            inflater, R.layout.view_pager, container, false
        )

        binding.pager.adapter = NewJourneyViewPagerAdapter(this)
        binding.pager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.pager, tabConfiguration).attach()

        observeNavigateToJourney()
        return binding.root

    }
}

private val NewJourneyFragment.tabConfiguration: TabLayoutMediator.TabConfigurationStrategy
    get() = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        when (position) {
            0 -> tab.text = getString(R.string.tab_start_text)
            1 -> tab.text = getString(R.string.tab_end_text)
            2 -> tab.text = getString(R.string.tab_time_text)
            else -> tab.text = getString(R.string.tab_info_text)
        }
    }

fun NewJourneyFragment.observeNavigateToJourney() {
    newJourneyViewModel.navigateToJourneyLiveData.observe(this) {
        it?.let {
            val action = NewJourneyFragmentDirections.actionNewJourneyFragmentToJourneyFragment(it)
            findNavController().navigate(action)
            newJourneyViewModel.completeNavigateToJourney()
        }
    }
}

package com.lokech.taxi.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lokech.taxi.R
import com.lokech.taxi.databinding.ViewPagerBinding

class JourneyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ViewPagerBinding>(
            inflater, R.layout.view_pager, container, false
        )

        binding.pager.adapter = JourneyViewPagerAdapter(this)
        binding.pager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.pager, tabConfiguration).attach()

        return binding.root

    }
}

private val JourneyFragment.tabConfiguration: TabLayoutMediator.TabConfigurationStrategy
    get() = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        when (position) {
            0 -> tab.text = getString(R.string.tab_route_text)
            else -> tab.text = getString(R.string.tab_info_text)
        }
    }

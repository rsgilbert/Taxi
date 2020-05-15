package com.lokech.taxi.newjourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
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

        binding.pager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.pager, tabConfiguration).attach()

        return binding.root

    }

    private val tabConfiguration =
        TabLayoutMediator.OnConfigureTabCallback { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_start_text)
                1 -> tab.text = getString(R.string.tab_end_text)
                else -> tab.text = getString(R.string.tab_info_text)
            }
        }
}
package com.lokech.taxi.journeys

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lokech.taxi.R
import com.lokech.taxi.databinding.JourneysListBinding
import com.lokech.taxi.util.repository
import org.jetbrains.anko.support.v4.toast

class JourneysFragment : Fragment() {
    val journeysViewModel: JourneysViewModel by viewModels {
        JourneysViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val binding: JourneysListBinding = DataBindingUtil.inflate(
            inflater, R.layout.journeys_list, container, false
        )

        binding.journeysViewModel = journeysViewModel
        binding.lifecycleOwner = this
        binding.journeys.adapter = JourneysAdapter(itemListClickListener)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_journeys, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> toast("Searching")
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


}

val JourneysFragment.itemListClickListener: JourneysAdapter.OnClickListener
    get() = JourneysAdapter.OnClickListener {
        val action = JourneysFragmentDirections.actionJourneysFragmentToJourneyFragment(it.id)
        findNavController().navigate(action)
    }


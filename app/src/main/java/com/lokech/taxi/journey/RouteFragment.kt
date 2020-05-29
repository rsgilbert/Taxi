package com.lokech.taxi.journey

import androidx.fragment.app.viewModels
import com.lokech.taxi.MapFragment
import com.lokech.taxi.util.repository


class RouteFragment : MapFragment() {
    val journeyViewModel: JourneyViewModel by viewModels(
        { requireParentFragment() }) {
        JourneyViewModelFactory(journeyId, repository)
    }

}

val RouteFragment.journeyId: Int
    get() = JourneyFragmentArgs.fromBundle(requireParentFragment().arguments!!).journeyId



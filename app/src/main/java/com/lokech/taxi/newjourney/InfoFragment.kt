package com.lokech.taxi.newjourney

import androidx.fragment.app.viewModels
import com.lokech.taxi.MapFragment

class InfoFragment : MapFragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    )
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val binding = DataBindingUtil.inflate<FragmentInfoBinding>(
//            inflater,
//            R.layout.fragment_info, container, false
//        )
//
//
//        return binding.root
//
//    }
}

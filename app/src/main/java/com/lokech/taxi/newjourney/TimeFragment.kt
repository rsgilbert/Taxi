package com.lokech.taxi.newjourney


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lokech.taxi.R
import com.lokech.taxi.databinding.FragmentTimeBinding
import com.lokech.taxi.dialogs.DateListener
import com.lokech.taxi.dialogs.MyDatePickerFragment
import com.lokech.taxi.dialogs.MyTimePickerFragment
import com.lokech.taxi.dialogs.TimeListener
import org.jetbrains.anko.support.v4.toast

class TimeFragment : Fragment(), TimeListener, DateListener {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTimeBinding>(
            inflater, R.layout.fragment_time, container, false
        )

        binding.newJourneyViewModel = newJourneyViewModel
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            timeSection.setOnClickListener {
                showMyTimePickerDialog()
            }
            dateSection.setOnClickListener {
                showMyDatePickerDialog()
            }
        }


        return binding.root
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        toast("$year $month $day")
        newJourneyViewModel.setDate(year, month + 1, day)
    }

    override fun setTime(hour: Int, minute: Int) {
        newJourneyViewModel.setTime(hour, minute)
    }
}


fun TimeFragment.showMyTimePickerDialog() {
    newJourneyViewModel.dateTimeLiveData.value?.let { localDateTime ->
        val timePickerDialog = MyTimePickerFragment().apply {
            arguments = Bundle().apply {
                putInt("hour", localDateTime.hour)
                putInt("minute", localDateTime.minute)
            }
        }
        timePickerDialog.show(childFragmentManager, "timePicker")
    }
}

fun TimeFragment.showMyDatePickerDialog() {
    newJourneyViewModel.dateTimeLiveData.value?.let { localDateTime ->
        val datePickerDialog = MyDatePickerFragment().apply {
            arguments = Bundle().apply {
                putInt("year", localDateTime.year)
                putInt("month", localDateTime.monthValue - 1)
                putInt("day", localDateTime.dayOfMonth)
            }
        }
        datePickerDialog.show(childFragmentManager, "datePicker")
    }
}
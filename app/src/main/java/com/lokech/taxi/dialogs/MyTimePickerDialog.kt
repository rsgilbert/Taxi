package com.lokech.taxi.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class MyTimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    lateinit var timeListener: TimeListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = arguments!!.getInt("hour")
        val minute = arguments!!.getInt("minute")

        return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        timeListener.setTime(hourOfDay, minute)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            timeListener = parentFragment as TimeListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement listener")
        }
    }
}

interface TimeListener {
    fun setTime(hour: Int, minute: Int)
}
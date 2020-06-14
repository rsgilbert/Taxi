package com.lokech.taxi.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment

class MyDatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    lateinit var dateListener: DateListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = arguments!!.getInt("year")
        val month = arguments!!.getInt("month")
        val day = arguments!!.getInt("day")
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateListener.setDate(year, month, day)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dateListener = parentFragment as DateListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement EditListener")
        }
    }
}

interface DateListener {
    fun setDate(year: Int, month: Int, day: Int)
}
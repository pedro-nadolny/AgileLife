package com.nadolny.pedro.agilelife.utils

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*
import android.view.ViewParent
import android.view.MotionEvent



object DateUtils {

    fun stringFrom(date: Date) : String {
        return SimpleDateFormat("dd/MM/yyy").format(date)
    }

    fun extenseStringFrom(date: Date) : String {
        return SimpleDateFormat("EEEE, d ").format(date) + "of " + SimpleDateFormat("MMMM, yyyy").format(date)
    }

    fun dateFrom(str: String) : Date {
        return SimpleDateFormat("dd/MM/yyy").parse(str)
    }

    fun dateFrom(datePicker: DatePicker) : Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    fun getDayFrom(date: Date) : Int {
        val str = SimpleDateFormat("dd").format(date)
        return str.toIntOrNull() ?: 0
    }

    fun getMonthFrom(date: Date) : Int {
        val str = SimpleDateFormat("MM").format(date)
        return str.toIntOrNull() ?: 0
    }

    fun getYearFrom(date: Date) : Int {
        val str = SimpleDateFormat("yyy").format(date)
        return str.toIntOrNull() ?: 0
    }
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

class BetterDatePicker(context: Context?, attrs: AttributeSet?) : DatePicker(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            val p = parent
            p?.requestDisallowInterceptTouchEvent(true)
        }

        return false
    }
}
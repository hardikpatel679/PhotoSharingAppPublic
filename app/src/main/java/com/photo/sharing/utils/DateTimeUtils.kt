package com.photo.sharing.utils

import android.text.format.DateFormat
import java.util.*

class DateTimeUtils {

    companion object {
        fun getDate(time: Long): String? {
            val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = time * 1000
            return DateFormat.format("dd MMMM yyyy", cal).toString()
        }
    }
}
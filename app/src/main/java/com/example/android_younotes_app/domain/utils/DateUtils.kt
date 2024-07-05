package com.example.android_younotes_app.domain.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDate(timestamp: Long, pattern: String = "MMM d, yyyy", locale: Locale = Locale.getDefault()): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(pattern, locale)
        return formatter.format(date)
    }
}
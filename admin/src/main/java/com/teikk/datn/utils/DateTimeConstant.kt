package com.teikk.datn.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeConstant {
    fun getCurrentTimestampStr(): String {
        val now = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(now)
    }

    fun datetimeToString(dateTime: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(dateTime)
    }

    fun stringToDateTime(dateTimeString: String): Date {
        // Try to parse using the "yyyy-MM-dd HH:mm:ss" format first
        return try {
            val formatterWithSpace = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatterWithSpace.parse(dateTimeString) ?: Date()
        } catch (e: ParseException) {
            // If it fails, try parsing with the "T" format
            val formatterWithT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatterWithT.parse(dateTimeString) ?: Date()
        }
    }
}

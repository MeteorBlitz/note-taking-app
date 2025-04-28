package com.example.note_taking_app.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, EEE", Locale.getDefault())  // "Apr 28, Mon" format
    val date = Date(timestamp)
    return dateFormat.format(date)
}
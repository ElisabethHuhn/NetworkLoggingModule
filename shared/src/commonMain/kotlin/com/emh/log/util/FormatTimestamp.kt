package com.emh.log.util

import kotlinx.datetime.*
import kotlinx.datetime.format.*

/**
 * Format the current timestamp in the format "YYYY-MM-DDTHH:MM:SSZ"
 */
const val validFormat = "yyyy-MM-dd'T'HH:mm:ss"

fun formatCurrentTimestamp() : String {
    val currentDateTime = getNow()
    return currentDateTime.format(
        LocalDateTime.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            dayOfMonth()
            char('T')
            hour()
            char(':')
            minute()
            char(':')
            second()
            char('Z')
        }
    )
}

fun getNow() : LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
}

fun getEpochMillis() : Long {
    return Clock.System.now().toEpochMilliseconds()
}


fun isTimestampFormatValid(
    timestamp: String,
    format: String = validFormat
): Boolean {
    return try {
        // Formats.ISO is used. 2023-01-02T23:40:57.120
        val isValid = LocalDateTime.parse(input = timestamp)
        true
    } catch (e: Exception) {
        false
    }
}
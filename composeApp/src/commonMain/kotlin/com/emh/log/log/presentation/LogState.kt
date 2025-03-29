package com.emh.log.log.presentation

import com.emh.log.domain.LogEntry
import com.emh.log.domain.LoggingMsgSeverity
import io.ktor.http.HttpStatusCode

data class LogState(
    val isLoading: Boolean = true,

    val bufferLength: Int = 11,
    val bufferDuration: Long = 1000L, // 1 second
    val numberToGenerate: Int = 10,

    val lastBuffered : LogEntry = LogEntry(
        message = "NO MESSAGE YET",
        timestamp = "NO TIMESTAMP YET",
        severity = LoggingMsgSeverity.NOT_ASSIGNED
    ),
    val lastSent : LogEntry = LogEntry(
        message = "NO MESSAGE YET",
        timestamp = "NO TIMESTAMP YET",
        severity = LoggingMsgSeverity.NOT_ASSIGNED
    ),

    val totalSent : Int = 0,
    val totalQueued : Int = 0,
    val totalSuccess: Int = 0,
    val totalError : Int = 0,
    val lastResponseString: String = "NO RESPONSE YET",
    val lastStatusCode : HttpStatusCode = HttpStatusCode.OK,

    val logGreeting : String = "Greeting not yet fetched",
    val logMessages : List<LogEntry> = emptyList()

)

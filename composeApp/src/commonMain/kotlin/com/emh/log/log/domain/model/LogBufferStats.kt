package com.emh.log.log.domain.model

import com.emh.log.domain.LogEntry
import com.emh.log.domain.LoggingMsgSeverity
import io.ktor.http.HttpStatusCode

data class LogBufferStats(
    var bufferSize : Int = 1,
    var totalSent : Int = 0,
    var numberQueued : Int = 0,
    var numberSuccess: Int = 0,
    var numberError : Int = 0,
    var lastQueued : LogEntry = LogEntry(
        message = "NO MESSAGE YET",
        timestamp = "NO TIMESTAMP YET",
        severity = LoggingMsgSeverity.NOT_ASSIGNED
    ),
    var lastSent : LogEntry = LogEntry(
        message = "NO MESSAGE YET",
        timestamp = "NO TIMESTAMP YET",
        severity = LoggingMsgSeverity.NOT_ASSIGNED
    ),
    var lastResponseString: String = "NO RESPONSE YET",
    var lastStatus: HttpStatusCode = HttpStatusCode.OK

    )

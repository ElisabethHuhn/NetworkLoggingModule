package com.emh.log.log.domain

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.emh.log.core.domain.DataError
import  com.emh.log.core.domain.Result
import com.emh.log.core.domain.onError
import com.emh.log.core.domain.onSuccess
import com.emh.log.domain.LogEntry
import com.emh.log.domain.LoggingMsgSeverity
import com.emh.log.log.domain.model.LogBufferStats
import com.emh.log.util.formatCurrentTimestamp
import com.emh.log.util.getEpochMillis
import io.ktor.http.HttpStatusCode

class LogBusinessLogic (
    private val logRepository: LogRepository
){
    var logCounter = 0
    var logEntries = mutableListOf<LogEntry>()

    fun setMaxBuffer(newSize: Int) {
        logBufferStats.bufferSize = newSize
    }

    private var bufferDuration = 1000L // 1 second
    fun setBufferDur(newDuration: Long) {
        bufferDuration = newDuration
    }

    var timeLastSent : Long = 0L

    val logBufferStats = LogBufferStats()

    val sendLock = Mutex(false)


    /*
     * Generate a number of log messages
     */
    suspend fun generateLogMessages(
        numberToGenerate: Int,
    ) : LogBufferStats {
        //initialize time last sent on initial call
        timeLastSent = getEpochMillis()

        for (i in 1..numberToGenerate) {
            //build a log message
            val timestamp = formatCurrentTimestamp()

            val severity = when {
                logCounter % 2 == 0 -> LoggingMsgSeverity.INFO
                logCounter % 3 == 0 -> LoggingMsgSeverity.ERROR
                logCounter % 5 == 0 -> LoggingMsgSeverity.WARN
                else -> LoggingMsgSeverity.DEBUG
            }
            val message = "Log message $logCounter"

            val logEntry = LogEntry(
                message = message,
                timestamp = timestamp,
                severity = severity,
                )
            logMessage(logEntry)

            logCounter++
        }

        return logBufferStats
    }

    suspend fun logMessage(logEntry: LogEntry) : LogBufferStats {
        logEntries.add(logEntry)

        val timeNowMs = getEpochMillis()
        val timeDif = timeNowMs - timeLastSent

        if ( (logEntries.size >= logBufferStats.bufferSize) ||
             (timeDif > bufferDuration)
        ) {
           flushBuffer()
        }
        logBufferStats.numberQueued = logEntries.size
        logBufferStats.lastQueued = logEntry

        //dd=eep copy the buffer stats
        val returnStats : LogBufferStats = LogBufferStats()
        returnStats.apply {
            bufferSize = logBufferStats.bufferSize
            totalSent = logBufferStats.totalSent
            numberQueued = logBufferStats.numberQueued
            numberSuccess = logBufferStats.numberSuccess
            numberError = logBufferStats.numberError
            lastQueued = logBufferStats.lastQueued.copy()
            lastSent = logBufferStats.lastSent.copy()
            lastResponseString = logBufferStats.lastResponseString
            lastStatus = logBufferStats.lastStatus
        }
        return returnStats
    }

    suspend fun flushBuffer() : Result<String, DataError.Remote>  {
        var response : Result<String, DataError.Remote>

        //lock out any other coroutines
        sendLock.withLock {
            val sendList = logEntries.toList()
            logEntries = mutableListOf<LogEntry>()
            timeLastSent = getEpochMillis()
            if (sendList.size > 0) {
                response = logRepository.sendLogBuffer(sendList)
                logBufferStats.totalSent += sendList.size
                logBufferStats.numberQueued = 0
                logBufferStats.lastResponseString = response.toString()
                logBufferStats.lastQueued = sendList.last()
                logBufferStats.lastSent = sendList.last()

                response.onSuccess {
                    logBufferStats.numberSuccess += sendList.size
                    logBufferStats.lastStatus = HttpStatusCode.OK
                }
                response.onError { it ->
                    logBufferStats.numberError += sendList.size
                    logBufferStats.lastStatus = HttpStatusCode.BadRequest
                }
                return response
            }
            return Result.Success("No log entries to send")
        }
    }
}
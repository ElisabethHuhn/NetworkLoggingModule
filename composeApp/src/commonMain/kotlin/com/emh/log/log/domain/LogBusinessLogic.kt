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
    private var logCounter = 0
    private var logEntries = mutableListOf<LogEntry>()
    private var timeLastSent : Long = 0L
    private val logBufferStats = LogBufferStats()
    private val sendLock = Mutex(false)

    fun setMaxBuffer(newSize: Int) {
        logBufferStats.bufferSize = newSize
    }

    private var bufferDuration = 1000L // 1 second
    fun setBufferDur(newDuration: Long) {
        bufferDuration = newDuration
    }


    /*
     * Generate a number of log messages
     */
    suspend fun generateLogMessages(
        numberToGenerate: Int,
    ) : LogBufferStats {
        //initialize time last sent on initial call
        if (timeLastSent == 0L) timeLastSent = getEpochMillis()

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

    private suspend fun logMessage(logEntry: LogEntry) : LogBufferStats {
        logEntries.add(logEntry)

        logBufferStats.lastQueued = logEntry
        logBufferStats.numberQueued = logEntries.size

        var timeExpired = false
        if (bufferDuration <= 0) {
            val timeNowMs = getEpochMillis()
            val timeDif = timeNowMs - timeLastSent
            if (timeDif > bufferDuration) {
                timeExpired = true
            }
        }

        val isBufferFull = logEntries.size >= logBufferStats.bufferSize

        return if ( isBufferFull || timeExpired) {
            flushBuffer() //updates logBufferStats
        } else {
            deepCopyStats()
        }
    }

    suspend fun flushBuffer() : LogBufferStats  {
        var response : Result<String, DataError.Remote>

        //lock out any other coroutines
        sendLock.withLock {
            val sendList = logEntries.toList()
            logEntries = mutableListOf()
            timeLastSent = getEpochMillis()
            if (sendList.isNotEmpty()) {
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
            }
            return deepCopyStats()
        }
    }

    private fun deepCopyStats() : LogBufferStats {
        val returnStats = LogBufferStats()

        //dd=eep copy the buffer stats
        returnStats.apply {
            bufferSize = logBufferStats.bufferSize
            totalSent = logBufferStats.totalSent
            numberQueued = logBufferStats.numberQueued
            numberSuccess = logBufferStats.numberSuccess
            numberError = logBufferStats.numberError
            lastSent = logBufferStats.lastSent.copy()
            lastResponseString = logBufferStats.lastResponseString
            lastStatus = logBufferStats.lastStatus
        }
        return returnStats
    }

    suspend fun fetchLogGreeting() : String {
        var returnString =  "Error fetching log greeting"

        val result = logRepository.fetchLogGreeting()
        result.onSuccess { successString ->
            returnString = successString
        }
       return returnString
    }

    suspend fun fetchLogMessages() : List<LogEntry> {
        var returnList =  listOf<LogEntry>()

        val result = logRepository.fetchLogMessages()
        result.onSuccess { successList ->
            returnList = successList
        }
       return returnList
    }

}
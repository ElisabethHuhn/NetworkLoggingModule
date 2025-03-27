package com.emh.log.log.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emh.log.domain.LogEntry
import com.emh.log.log.domain.LogBusinessLogic
import com.emh.log.log.domain.model.LogBufferStats
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoggerViewModel (
    private val logBusinessLogic: LogBusinessLogic
) : ViewModel() {

    /* ********************************************************************
     * State values displayed in MainScreen UI
     */
    private val _state = MutableStateFlow(LoggerState())
    //if there needs to be initialization, use ,onStart{} and .stateIn{}
    val state = _state.asStateFlow()


    /*
     * actions triggered by User in MainScreen UI
     */
    fun onAction(action: LoggerAction) {
        when(action) {
            is LoggerAction.UpdateLoggerBufferLength -> {
                updateLoggerBufferLength(action.lengthString)
            }
            is LoggerAction.UpdateLoggerBufferDuration -> {
                updateLoggerBufferDuration(action.durationString)
            }
            is LoggerAction.UpdateNumberToGenerateChanged -> {
                updateNumberToGenerate(action.number)
            }
            is LoggerAction.GenerateLogMessages -> {
                generateLogMessages()
            }
            is LoggerAction.FlushLogBuffer -> {
                flushLogBuffer()
            }
            is LoggerAction.FetchLogMessages -> {
                fetchLogMessages()
            }
            is LoggerAction.FetchLogGreeting -> {
                fetchLogGreeting()
            }



            else -> {}
        }
    }

    /*
     * functions to update the UI State values displayed in MainScreen UI
     */
    private fun updateIsLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    private fun updateNumberToGenerate(newCounter: String) {
        val numberToGenerate = if (newCounter != "") {
            try{
                newCounter.toInt()
            } catch (e: NumberFormatException) {
                0
            }
        } else {
            0
        }
        _state.update { it.copy(numberToGenerate = numberToGenerate) }
    }
    private fun updateLoggerBufferLength(lengthString: String) {
        val length = if (lengthString != "") {
            try{
                lengthString.toInt()
            } catch (e: NumberFormatException) {
                0
            }
        } else {
            0
        }
        _state.update { it.copy(bufferLength = length) }
        logBusinessLogic.setMaxBuffer(length)
    }
    private fun updateLoggerBufferDuration(durationString: String) {
        val duration =  if (durationString != "") {
            try{
                durationString.toLong()
            } catch (e: NumberFormatException) {
                0L
            }
        } else {
            0L
        }
        _state.update { it.copy(bufferDuration = duration) }
        logBusinessLogic.setBufferDur(duration)
    }

    private fun updateLastQueued(logEntry: LogEntry) {
        _state.update { it.copy(lastBuffered = logEntry) }
    }
    private fun updateLastSent(logEntry: LogEntry) {
        _state.update { it.copy(lastSent = logEntry) }
    }
    private fun updateTotalSent(number: Int) {
        _state.update { it.copy(totalSent = number) }
    }
    private fun updateTotalQueued(number: Int) {
        _state.update { it.copy(totalQueued = number) }
    }
    private fun updateTotalSuccess(number: Int) {
        _state.update { it.copy(totalSuccess = number) }
    }
    private fun updateTotalError(number: Int) {
        _state.update { it.copy(totalError = number) }
    }
    private fun updateLastResponseString(responseString: String) {
        _state.update { it.copy(lastResponseString = responseString) }
    }
    private fun updateLastHttpStatus(status: HttpStatusCode) {
        _state.update { it.copy(lastStatusCode = status) }
    }
    private fun updateLogMessages(messages: List<LogEntry>) {
        _state.update { it.copy(logMessages = messages) }
    }
    private fun updateLogGreeting(greeting: String) {
        _state.update { it.copy(logGreeting = greeting) }
    }


    /*
     * functions to carry out the UI actions triggered by User in MainScreen UI
     */

    private fun generateLogMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            val logBufferStats =
                logBusinessLogic.generateLogMessages(_state.value.numberToGenerate)

            //update the state with the result
           updateStats(logBufferStats)
        }
    }

    private fun flushLogBuffer() {
        viewModelScope.launch (Dispatchers.IO) {
            val result = logBusinessLogic.flushBuffer()
            updateStats(result)
        }
    }

    private fun updateStats(logBufferStats: LogBufferStats) {
        updateLoggerBufferLength(logBufferStats.bufferSize.toString())
        updateTotalSent(logBufferStats.totalSent)
        updateTotalQueued(logBufferStats.numberQueued)
        updateTotalSuccess(logBufferStats.numberSuccess)
        updateTotalError(logBufferStats.numberError)
        updateLastQueued(logBufferStats.lastQueued)
        updateLastSent(logBufferStats.lastSent)
        updateLastResponseString(logBufferStats.lastResponseString)
        updateLastHttpStatus(logBufferStats.lastStatus)
    }

    private fun fetchLogMessages() {
        viewModelScope.launch (Dispatchers.IO) {
            val result = logBusinessLogic.fetchLogMessages()
            updateLogMessages(result)
        }
    }

    private fun fetchLogGreeting() {
        viewModelScope.launch (Dispatchers.IO) {
            val result = logBusinessLogic.fetchLogGreeting()
            updateLogGreeting(result)
        }
    }
}
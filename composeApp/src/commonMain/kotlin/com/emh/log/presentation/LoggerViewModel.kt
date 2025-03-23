package com.emh.log.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.emh.log.core.domain.onError
import com.emh.log.core.domain.onSuccess
import com.emh.log.domain.LogEntry
import com.emh.log.log.domain.LogBusinessLogic
import io.ktor.http.HttpStatusCode

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

            else -> {}
        }
    }

    /*
     * functions to update the UI State values displayed in MainScreen UI
     */
    fun updateIsLoading(isLoading: Boolean) {
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

    fun updateLastQueued(logEntry: LogEntry) {
        _state.update { it.copy(lastBuffered = logEntry) }
    }
    fun updateLastSent(logEntry: LogEntry) {
        _state.update { it.copy(lastSent = logEntry) }
    }
    fun updateTotalSent(number: Int) {
        _state.update { it.copy(totalSent = number) }
    }
    fun updateTotalQueued(number: Int) {
        _state.update { it.copy(totalQueued = number) }
    }
    fun updateTotalSuccess(number: Int) {
        _state.update { it.copy(totalSuccess = number) }
    }
    fun updateTotalError(number: Int) {
        _state.update { it.copy(totalError = number) }
    }
    fun updateLastResponseString(responseString: String) {
        _state.update { it.copy(lastResponseString = responseString) }
    }
    fun updateLastHttpStatus(status: HttpStatusCode) {
        _state.update { it.copy(lastStatusCode = status) }
    }




    /*
     * functions to carry out the UI actions triggered by User in MainScreen UI
     */

    private fun generateLogMessages() {
        viewModelScope.launch {
            val logBufferStats =
                logBusinessLogic.generateLogMessages(_state.value.numberToGenerate)

            //update the state with the result

            updateTotalSent(logBufferStats.totalSent)
            updateTotalQueued(logBufferStats.numberQueued)
            updateTotalSuccess(logBufferStats.numberSuccess)
            updateTotalError(logBufferStats.numberError)
            updateLastResponseString(logBufferStats.lastResponseString)
            updateLastHttpStatus(logBufferStats.lastStatus)
            updateLastQueued(logBufferStats.lastQueued)
            updateLastSent(logBufferStats.lastSent)
        }
    }

    private fun flushLogBuffer() {
        viewModelScope.launch {
            val result = logBusinessLogic.flushBuffer()
            result.onSuccess { responseString ->
               updateLastResponseString(responseString)
            }
            result.onError { it ->
                //todo update the state with the result
                updateLastHttpStatus(HttpStatusCode.BadRequest)
            }
        }
    }


}
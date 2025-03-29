package com.emh.log.log.presentation

sealed interface LogAction {
    data object OnBackClick: LogAction
    data class UpdateNumberToGenerateChanged(val number: String): LogAction
    data class UpdateLogBufferLength(val lengthString: String): LogAction
    data class UpdateLogBufferDuration(val durationString: String): LogAction
    data object GenerateLogMessages: LogAction
    data object FlushLogBuffer: LogAction
    data object FetchLogMessages: LogAction
    data object FetchLogGreeting: LogAction

}
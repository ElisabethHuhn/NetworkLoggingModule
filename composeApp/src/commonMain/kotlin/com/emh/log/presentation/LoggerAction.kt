package com.emh.log.presentation

sealed interface LoggerAction {
    data object OnBackClick: LoggerAction
    data class UpdateNumberToGenerateChanged(val number: String): LoggerAction
    data class UpdateLoggerBufferLength(val lengthString: String): LoggerAction
    data class UpdateLoggerBufferDuration(val durationString: String): LoggerAction
    data object GenerateLogMessages: LoggerAction
    data object FlushLogBuffer: LoggerAction
}
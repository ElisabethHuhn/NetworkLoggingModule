package com.emh.log.domain

import kotlinx.serialization.Serializable

@Serializable
data class LogEntry(
    val message: String,
    val timestamp: String,
    val severity: LoggingMsgSeverity
)

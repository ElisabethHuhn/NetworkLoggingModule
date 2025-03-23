package com.emh.log.domain



//@Serializable
data class LogEntryBuffer(
    /* @SerialName("null") */ val entries: MutableList<LogEntry> = mutableListOf()
)

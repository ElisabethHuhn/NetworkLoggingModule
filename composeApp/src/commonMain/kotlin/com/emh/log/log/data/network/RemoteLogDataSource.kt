package com.emh.log.log.data.network

import com.emh.log.core.domain.DataError
import com.emh.log.core.domain.Result
import com.emh.log.domain.LogEntry

interface RemoteLogDataSource {
    suspend fun logRemote(
        logItemsList: List<LogEntry>
    ): Result<String, DataError.Remote>
}
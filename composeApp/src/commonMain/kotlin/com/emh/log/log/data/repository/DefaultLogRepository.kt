package com.emh.log.log.data.repository


import com.emh.log.core.domain.DataError
import com.emh.log.core.domain.Result
import com.emh.log.domain.LogEntry
import com.emh.log.log.data.network.RemoteLogDataSource
import com.emh.log.log.domain.LogRepository


class DefaultLogRepository(
    private val remoteLogDataSource: RemoteLogDataSource,
): LogRepository {
    override suspend fun sendLogBuffer(
        logItemsList: List<LogEntry>
    ): Result<String, DataError.Remote> {
        return remoteLogDataSource.logRemote(logItemsList)
    }
}
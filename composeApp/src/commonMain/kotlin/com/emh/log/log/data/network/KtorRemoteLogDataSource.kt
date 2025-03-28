package com.emh.log.log.data.network

import com.emh.log.core.domain.Result
import com.emh.log.CLIENT_LOCAL_URL
import com.emh.log.SERVER_PORT
import com.emh.log.core.data.safeCall
import com.emh.log.core.domain.DataError
import com.emh.log.domain.LogEntry
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType



class KtorRemoteLogDataSource(
    private val httpClient: HttpClient
): RemoteLogDataSource {

    override suspend fun logRemote(
        logItemsList: List<LogEntry>
    ): Result<String, DataError.Remote> {

//        val url = "$CLIENT_BASE_URL:$SERVER_PORT/log"
//        val url = "$CLIENT_LOGGER_HOST:$SERVER_PORT/log"
        val url = "$CLIENT_LOCAL_URL:$SERVER_PORT/log"

        return safeCall<String> {
            httpClient.post(
                urlString =  url
            ) {
                contentType(ContentType.Application.Json)
                setBody(logItemsList)
            }
        }
    }

    override suspend fun fetchLogGreeting(): Result<String, DataError.Remote> {
//        val url = "$CLIENT_BASE_URL:$SERVER_PORT"
        val url = "$CLIENT_LOCAL_URL:$SERVER_PORT"

        return safeCall<String> {
            httpClient.get(
                urlString =  url
            )
        }
    }

    override suspend fun fetchLogMessages(): Result<List<LogEntry>, DataError.Remote> {
        val url = "$CLIENT_LOCAL_URL:$SERVER_PORT/log"

        return safeCall<List<LogEntry>> {
            httpClient.get(
                urlString = url
            )
        }


    }
}
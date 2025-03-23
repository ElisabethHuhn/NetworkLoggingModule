package com.emh.log.log.data.network

import com.emh.log.core.domain.Result
import com.emh.log.BASE_URL
import com.emh.log.SERVER_PORT
import com.emh.log.core.data.safeCall
import com.emh.log.core.domain.DataError
import com.emh.log.domain.LogEntry
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


//private const val BASE_URL = "http://localhost:5000/log"
//private const val BASE_URL = "https://ehuhn.free.beeceptor.com/log"

class KtorRemoteLogDataSource(
    private val httpClient: HttpClient
): RemoteLogDataSource {

    override suspend fun logRemote(
        logItemsList: List<LogEntry>
    ): Result<String, DataError.Remote> {
        val url = "$BASE_URL:$SERVER_PORT/log"
        return safeCall<String> {
            httpClient.post(
                urlString =  url
            ) {
                contentType(ContentType.Application.Json)
                setBody(logItemsList)
            }
        }
    }
}
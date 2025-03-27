package com.emh.log.core.data

import com.emh.log.core.domain.DataError
import com.emh.log.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownServiceException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch(e: SocketTimeoutException) {
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: UnresolvedAddressException) {
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch(e: SerializationException) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.SERIALIZATION)
    } catch(e: ConnectException) {
        coroutineContext.ensureActive()
        println(e.stackTrace)
        return Result.Error(DataError.Remote.CONNECTION_FAILED)
    } catch(e: HttpRequestTimeoutException) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch(e: UnknownServiceException) {
        coroutineContext.ensureActive()
        println(e.stackTrace)
        return Result.Error(DataError.Remote.UNKNOWN_SERVICE)
    } catch(e: SocketException) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.SOCKET_EXCEPTION)
    } catch (e: Exception) {
        println(e.message)
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch(e: NoTransformationFoundException) {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.SERVER)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}
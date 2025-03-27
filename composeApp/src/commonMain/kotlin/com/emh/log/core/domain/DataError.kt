package com.emh.log.core.domain

sealed interface DataError: Error {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,    //408
        TOO_MANY_REQUESTS,  //429
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN_SERVICE,
        SOCKET_EXCEPTION,
        CONNECTION_FAILED,
        UNAUTHORIZED,        //401
        CONFLICT,            //409
        PAYLOAD_TOO_LARGE,   //413
        SERVER_ERROR,       //500 to 599
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }
}
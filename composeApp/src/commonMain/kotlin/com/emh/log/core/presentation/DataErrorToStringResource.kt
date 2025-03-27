package com.emh.log.core.presentation

import com.emh.log.core.domain.DataError
import networkloggingmodule.composeapp.generated.resources.Res
import networkloggingmodule.composeapp.generated.resources.error_conflict
import networkloggingmodule.composeapp.generated.resources.error_connection_failed
import networkloggingmodule.composeapp.generated.resources.error_disk_full
import networkloggingmodule.composeapp.generated.resources.error_no_internet
import networkloggingmodule.composeapp.generated.resources.error_request_timeout
import networkloggingmodule.composeapp.generated.resources.error_serialization
import networkloggingmodule.composeapp.generated.resources.error_socket
import networkloggingmodule.composeapp.generated.resources.error_too_many_requests
import networkloggingmodule.composeapp.generated.resources.error_unauthorized
import networkloggingmodule.composeapp.generated.resources.error_unknown
import networkloggingmodule.composeapp.generated.resources.error_unknown_service
import networkloggingmodule.composeapp.generated.resources.error_payload_too_large
import networkloggingmodule.composeapp.generated.resources.error_server_error


fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.UNKNOWN_SERVICE -> Res.string.error_unknown_service
        DataError.Remote.SOCKET_EXCEPTION -> Res.string.error_socket
        DataError.Remote.CONNECTION_FAILED -> Res.string.error_connection_failed
        DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorized
        DataError.Remote.CONFLICT -> Res.string.error_conflict
        DataError.Remote.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
        DataError.Remote.SERVER_ERROR -> Res.string.error_server_error
    }
    
    return UiText.StringResourceId(stringRes)
}
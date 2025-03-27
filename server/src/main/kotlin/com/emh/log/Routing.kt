package com.emh.log


import com.emh.log.domain.LogEntry
import com.emh.log.domain.LoggingMsgSeverity
import com.emh.log.util.isTimestampFormatValid
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.contentLength
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing


val logEntryStorage = mutableListOf<LogEntry>()

fun Application.configureRouting() {
    // routing {} installs the Routing plugin
    routing {
        // Routes define paths that your server responds to
        // Routes are processed in the order they are defined.
        // The first route that matches the request is the one that is executed.

        // The get function defines a route that responds to GET requests.
        get("/") {
            call.respondText("Ktor: Hello, World!")
        }
        get("http://10.0.2.2/") {
            call.respondText("Ktor: Hello, From your LOG server!")
        }

        // Static resources are files that are served directly by the server without any processing.

        // The staticResources function is a helper function that serves static resources from a directory.
        // The first parameter is the path to the directory containing the resources.
        // The second parameter is the path to the URL that the resources will be served from.
        // In this case, the resources are served from the static directory and are available at the /static URL.
//        staticResources("static", "static")

//        route("/log") {
            get ("/log"){
                call.respond(logEntryStorage)
            }

            post ("/log"){
                if (call.request.contentLength() == 0L) {
                    call.respondText(
                        text = "Request body is empty",
                        status = HttpStatusCode.BadRequest
                    )
                } else {
//                    val rawJson = call.receiveText()
//                    if (rawJson.isEmpty()) call.respondText (
//                        text = "Request body is empty",
//                        status = HttpStatusCode.BadRequest
//                    )


                    val logEntries : List<LogEntry>? = try {
                        call.receive<List<LogEntry> >()
                    } catch (e: Exception) {
                        println(e.message)
                        null
                    }

                    if (logEntries == null) {
                        call.respondText(
                            text = "Invalid JSON format",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                    else {
                        if (logEntries.isEmpty())
                            call.respondText(
                                text = "No log entries found",
                                status = HttpStatusCode.BadRequest
                            )

                        //validate the LogEntry objects
                        val invalidEntries = logEntries.filter { logEntry ->
                            logEntry.message == null ||
                                    logEntry.severity == null ||
                                    logEntry.timestamp == null
                        }
                        if (invalidEntries.isNotEmpty()) {
                            call.respondText(
                                text = "Missing required fields: ${invalidEntries.joinToString { it.message }}",
                                status = HttpStatusCode.BadRequest
                            )
                        }

                        val invalidSeverity = logEntries.filter { logEntry ->
                            isSeverityInvalid(logEntry.severity)
                        }
                        if (invalidSeverity.isNotEmpty()) {
                            call.respondText(
                                text = "Invalid severity level. Must be one of: ${LoggingMsgSeverity.entries.joinToString()}",
                                status = HttpStatusCode.BadRequest
                            )
                        }

                        val invalidTimestamp = logEntries.filter { isTimestampValid(it.timestamp) }
                        if (invalidTimestamp.isNotEmpty()) {
                            call.respondText(
                                text = "Invalid timestamp format",
                                status = HttpStatusCode.BadRequest
                            )
                        }

                        //store the log entries in the database
                        logEntries.forEach { logEntryStorage.add(it) }

                        //respond with a success message
                        call.respondText(
                            text = "Log entries stored successfully",
                            status = HttpStatusCode.OK
                        )
                    }
                }
            }
//        }

        //use coroutines
//        get("/data") {
//            val result = async { dataService.fetchData() }.await()
//            call.respond(result)
//        }
        //Wrap asynchronous calls in try-catch blocks to handle errors gracefully and prevent resource leaks.

    }
}

fun isSeverityInvalid(severity: LoggingMsgSeverity): Boolean {
    if (severity == LoggingMsgSeverity.NOT_ASSIGNED) return true
    val isValid : Boolean = LoggingMsgSeverity.entries.any { loggingMsgSeverity ->
        loggingMsgSeverity == severity
    }
    return !isValid
}

fun isTimestampValid(timestamp: String?): Boolean {
       if (timestamp.isNullOrEmpty()) return false
       return isTimestampFormatValid(timestamp = timestamp)
}

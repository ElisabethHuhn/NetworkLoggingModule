package com.emh.log


import com.emh.log.data.LogEntryDto
import com.emh.log.domain.LoggingMsgSeverity
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.contentLength
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing


val logEntryStorage = mutableListOf<LogEntryDto>()

fun Application.configureRouting() {
    routing {
        // Routes define paths that your server responds to
        // Routes are processed in the order they are defined.
        // The first route that matches the request is the one that is executed.

        // The get function defines a route that responds to GET requests.
        get("/") {
            call.respondText("Ktor: Hello, World!")
        }

        // Static resources are files that are served directly by the server without any processing.

        // The staticResources function is a helper function that serves static resources from a directory.
        // The first parameter is the path to the directory containing the resources.
        // The second parameter is the path to the URL that the resources will be served from.
        // In this case, the resources are served from the static directory and are available at the /static URL.
        staticResources("static", "static")

        route("/log") {
            get {
                call.respond(logEntryStorage)
            }

            post {
                if (call.request.contentLength() == 0L) {
                    call.respondText(
                        text = "Request body is empty",
                        status = HttpStatusCode.BadRequest
                    )
                } else {
//                    val rawJson = call.receiveText()

                    val logEntries : List<LogEntryDto>? = try {
                        call.receive<List<LogEntryDto>>()
                    } catch (e: Exception) {null}

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
                                text = "Missing required fields: ${invalidEntries.joinToString { it.message.toString() }}",
                                status = HttpStatusCode.BadRequest
                            )
                        }

                        val invalidSeverity = logEntries.filter { logEntry ->
                            isSeverityValid(logEntry.severity ?: "")
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
        }

        //use coroutines
//        get("/data") {
//            val result = async { dataService.fetchData() }.await()
//            call.respond(result)
//        }
        //Wrap asynchronous calls in try-catch blocks to handle errors gracefully and prevent resource leaks.

    }
}

fun isSeverityValid(severity: String): Boolean {
    return severity in LoggingMsgSeverity.entries.map { it.name }
}
fun isTimestampValid(timestamp: String?): Boolean {
   try {
       if (timestamp.isNullOrEmpty()) return false
       //assure the timestamp is a long integer
       timestamp.toLong()
       return true
   } catch (e: Exception) {
       return false
   }
}

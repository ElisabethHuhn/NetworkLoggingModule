package com.emh.log

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = SERVER_PORT,
        host =  "0.0.0.0",
        module = Application::module
    ).also { server ->
        server.start(wait = true)
    }
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
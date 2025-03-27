package com.emh.log

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        factory = Netty,
        port = SERVER_PORT,
        host =  SERVER_URL,
        module = Application::module
    ).also { server ->
        server.start(wait = true)
    }
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
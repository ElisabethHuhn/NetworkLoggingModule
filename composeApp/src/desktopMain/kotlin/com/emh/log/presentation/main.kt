package com.emh.log.presentation

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.emh.log.app.App
import com.emh.log.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "NetworkLoggingModule",
        ) {
            App()
        }
    }
}

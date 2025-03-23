package com.emh.log.di

import com.emh.log.presentation.LoggerViewModel
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule: Module  = module {
    single<HttpClientEngine> { OkHttp.create() }
    viewModelOf(::LoggerViewModel)
}
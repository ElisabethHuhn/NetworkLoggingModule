package com.emh.log.di


import com.emh.log.core.data.HttpClientFactory
import com.emh.log.log.data.network.RemoteLogDataSource
import com.emh.log.log.data.network.KtorRemoteLogDataSource

import com.emh.log.log.domain.LogBusinessLogic
import com.emh.log.log.domain.LogRepository
import com.emh.log.log.data.repository.DefaultLogRepository
import com.emh.log.log.presentation.LogViewModel

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf

import org.koin.core.module.Module

import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
//    single<HttpClientEngine> { OkHttp.create() }

    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteLogDataSource).bind<RemoteLogDataSource>()
    singleOf(::DefaultLogRepository).bind<LogRepository>()

    single { LogBusinessLogic(get()) }
    viewModelOf(::LogViewModel)

}

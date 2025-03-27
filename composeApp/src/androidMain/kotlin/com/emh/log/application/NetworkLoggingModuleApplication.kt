package com.emh.log.application

import android.app.Application
import com.emh.log.di.initKoin
import org.koin.android.ext.koin.androidContext


class NetworkLoggingModuleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@NetworkLoggingModuleApplication)
        }
    }
}
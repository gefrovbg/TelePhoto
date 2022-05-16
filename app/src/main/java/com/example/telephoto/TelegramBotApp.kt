package com.example.telephoto

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class TelegramBotApp : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
        startKoin {
            androidLogger()
            androidContext(context)
            modules()
        }
    }

    companion object {
        var application: Application? = null
            private set
        val context: Context
            get() = application!!.applicationContext

    }
}
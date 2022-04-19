package com.example.telephoto

import android.app.Application
import android.content.Context
import com.example.telephoto.domain.models.ChatId


class TelegramBotApp : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        var application: Application? = null
            private set
        val context: Context
            get() = application!!.applicationContext

        val listChatId
            get() = arrayListOf<ChatId>()

    }
}
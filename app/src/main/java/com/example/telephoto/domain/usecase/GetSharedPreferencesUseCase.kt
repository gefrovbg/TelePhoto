package com.example.telephoto.domain.usecase

import android.content.Context
import android.content.SharedPreferences
import com.example.telephoto.TelegramBotApp

class GetSharedPreferencesUseCase {

    fun execute(): SharedPreferences{
        val context = TelegramBotApp.context
        return context.getSharedPreferences("TelegramBot" , Context.MODE_PRIVATE)
    }

}
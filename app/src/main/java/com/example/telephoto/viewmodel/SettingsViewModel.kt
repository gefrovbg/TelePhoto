package com.example.telephoto.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.elbekD.bot.Bot
import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.usecase.GetAllClientUseCase
import com.example.telephoto.domain.usecase.GetTokenFromSharedPreferencesUseCase
import com.example.telephoto.domain.usecase.SaveTokenToSharedPreferencesUseCase
import com.example.telephoto.presentation.adapters.ClientAdapter
import com.example.telephoto.telegrambot.TelegramBotHostUseCase
import com.example.telephoto.telegrambot.repository.TelegramBotRepositoryImpl

class SettingsViewModel(
    private val saveTokenToSharedPreferencesUseCase: SaveTokenToSharedPreferencesUseCase,
    private val getTokenFromSharedPreferencesUseCase: GetTokenFromSharedPreferencesUseCase,
    private val getAllClientUseCase: GetAllClientUseCase,
    private val telegramBotHostUseCase: TelegramBotHostUseCase,
    private val telegramBotRepository: TelegramBotRepositoryImpl

): ViewModel() {

    private lateinit var bot: Bot

    private val allClient = getAllClientUseCase.execute()
    val adapter = ClientAdapter(allClient)
    val tokenString = getTokenFromSharedPreferencesUseCase.execute()?.token?: "Insert Token!"

    init {
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            if (it.token != ""){
                bot = telegramBotHostUseCase.execute(it)
                telegramBotRepository.execute(bot, null, adapter)
                try {
                    bot.start()
                }catch (e: Exception){
                    Log.i("Bot", "${e.message}")
                }
            }
        }
    }

    fun saveToken(token: String){
        val tokenToSave = Token(token)
        try {
            bot.stop()
        }catch (e: Exception){
            Log.i("Bot", "${e.message}")
        }
        saveTokenToSharedPreferencesUseCase.execute(tokenToSave)
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            bot = telegramBotHostUseCase.execute(it)
            telegramBotRepository.execute(bot, null, adapter)
            try {
                bot.start()
            }catch (e: Exception){
                Log.i("Bot", "${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            if (it.token != "") {
                bot.stop()
            }
        }
    }
}
package com.example.telephoto.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telephoto.data.repository.DataBaseRepositoryImpl
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.domain.usecase.GetAllClientUseCase
import com.example.telephoto.domain.usecase.GetTokenFromSharedPreferencesUseCase
import com.example.telephoto.domain.usecase.SaveTokenToSharedPreferencesUseCase
import com.example.telephoto.telegrambot.TelegramBotHostUseCase
import com.example.telephoto.telegrambot.repository.TelegramBotRepositoryImpl
import com.example.telephoto.viewmodel.SettingsViewModel

class SettingsViewModelFactory(contextApp: Context): ViewModelProvider.Factory {

    private val tokenSharedPreferencesRepository by lazy { TokenSharedPreferencesRepositoryImpl(contextApp) }
    private val getTokenFromSharedPreferencesUseCase by lazy { GetTokenFromSharedPreferencesUseCase(tokenSharedPreferencesRepository) }
    private val saveTokenToSharedPreferencesUseCase by lazy { SaveTokenToSharedPreferencesUseCase(tokenSharedPreferencesRepository) }
    private val telegramBotHostUseCase = TelegramBotHostUseCase()
    private val dataBaseRepository by lazy { DataBaseRepositoryImpl(contextApp, null) }
    private val getAllClientUseCase = GetAllClientUseCase(dataBaseRepository)
    private val telegramBotRepository by lazy { TelegramBotRepositoryImpl() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            saveTokenToSharedPreferencesUseCase = saveTokenToSharedPreferencesUseCase,
            getAllClientUseCase = getAllClientUseCase,
            telegramBotHostUseCase = telegramBotHostUseCase,
            getTokenFromSharedPreferencesUseCase = getTokenFromSharedPreferencesUseCase,
            telegramBotRepository = telegramBotRepository
        ) as T
    }
}
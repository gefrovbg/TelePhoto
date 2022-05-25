package com.example.telephoto.viewmodel

import TelegramBotMessageRepositoryAppImpl.Companion.tukTukClient
import android.content.SharedPreferences
//import android.util.Log
import androidx.lifecycle.*
import com.example.telephoto.data.repository.DescriptionSharedPreferencesRepositoryImpl.Companion.DESC_PREF_NAME
import com.example.telephoto.data.storage.sharedprefs.SharedPreferencesRepository
import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.usecase.*
import com.example.telephoto.presentation.adapters.ClientAdapter
//import kotlinx.coroutines.*

class SettingsViewModel(
    private val saveTokenToSharedPreferencesUseCase: SaveTokenToSharedPreferencesUseCase,
    private val getTokenFromSharedPreferencesUseCase: GetTokenFromSharedPreferencesUseCase,
    getAllClientUseCase: GetAllClientUseCase,
    getDescriptionFromSharedPreferencesUseCase: GetDescriptionFromSharedPreferencesUseCase,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    lifecycleOwner: LifecycleOwner,
    private val telegramBotUseCase: TelegramBotUseCase

): ViewModel() {

    private val allClient = getAllClientUseCase.execute()
    val adapter = ClientAdapter(allClient)
    val tokenString = MutableLiveData<String>()
    private val booleanDescription = MutableLiveData<Boolean>()
    val showDescriptionBoolean: LiveData<Boolean> = booleanDescription

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when(key){
            DESC_PREF_NAME -> booleanDescription.value = getDescriptionFromSharedPreferencesUseCase.execute()
        }
    }

    init {
        tokenString.value = getTokenFromSharedPreferencesUseCase.execute()?.token?: "Insert Token!"
        booleanDescription.value = getDescriptionFromSharedPreferencesUseCase.execute()
        sharedPreferencesRepository.getSharedSharedPreferences().registerOnSharedPreferenceChangeListener(listener)
        tukTukClient.observe(lifecycleOwner) {
            adapter.insertItem(
                Client(
                    it.chatId,
                    it.firstName,
                    it.lastName,
                    it.nickname,
                    it.addStatus
                )
            )
        }
//        Log.d("viewModel", " \nCreated")
//        viewModelScope.launch {
//            delay(1000)
//            val result = withContext(Dispatchers.Default){
//                val part1 = async {
//                    delay(1000)
//                    return@async "Part 1 done!"
//                }
//                val part2 = async {
//                    delay(2000)
//                    return@async "Part 2 done!"
//                }
//                val part3 = async {
//                    delay(3000)
//                    return@async "Part 3 done!"
//                }
//                val result1 = part1.await()
//                val result2 = part2.await()
//                val result3 = part3.await()
//
//                return@withContext " \n$result1\n$result2\n$result3"
//            }
//            Log.d("viewModel", result)
//        }
    }

    fun saveToken(token: String){
        tokenString.value = token
        val tokenToSave = Token(token)
        saveTokenToSharedPreferencesUseCase.execute(tokenToSave)
        telegramBotUseCase.stop()
        telegramBotUseCase.start()
    }


    override fun onCleared() {
        super.onCleared()
        sharedPreferencesRepository.getSharedSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener)
    }

}
package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.repository.TokenSharedPreferencesRepository

class SaveTokenToSharedPreferencesUseCase(private val tokenFromSharedPreferencesRepository: TokenSharedPreferencesRepository) {

    fun execute(token: Token):Boolean{
        return tokenFromSharedPreferencesRepository.saveTokenToSharedPreferences(token = token)
    }

}
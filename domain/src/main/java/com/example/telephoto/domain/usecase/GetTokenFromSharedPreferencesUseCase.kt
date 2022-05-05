package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.repository.TokenSharedPreferencesRepository


class GetTokenFromSharedPreferencesUseCase(private val tokenFromSharedPreferencesRepository: TokenSharedPreferencesRepository)  {

    fun execute() : Token? {
        return tokenFromSharedPreferencesRepository.getTokenFromSharedPreferences()
    }

}
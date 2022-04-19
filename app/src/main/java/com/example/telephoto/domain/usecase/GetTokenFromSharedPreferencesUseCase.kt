package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Token


class GetTokenFromSharedPreferencesUseCase  {

    private val getSharedPreferencesUseCase = GetSharedPreferencesUseCase()

    fun execute() : Token? {
        val shared = getSharedPreferencesUseCase.execute()
        val getToken = shared.getString("token", "")
        return getToken?.let { Token(it) }
    }

}
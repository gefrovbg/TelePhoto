package com.example.telephoto.data.repository

import android.content.Context
import com.example.telephoto.data.storage.sharedprefs.SharedPreferenceRepositoryImpl
import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.repository.TokenSharedPreferencesRepository

private const val TOKEN_PREF_NAME = "token"

class TokenSharedPreferencesRepositoryImpl(context: Context): TokenSharedPreferencesRepository {

    private val sharedPreferencesRepository = SharedPreferenceRepositoryImpl(context = context)

    override fun getTokenFromSharedPreferences(): Token? {

        return sharedPreferencesRepository.getStringFromSharedPreferences(TOKEN_PREF_NAME)
            ?.let { tokenSharedPrefToDomain(it) }

    }

    override fun saveTokenToSharedPreferences(token: Token):Boolean {

        return sharedPreferencesRepository.saveStringFromSharedPreferences(TOKEN_PREF_NAME, valueReference = tokenToData(token))

    }

    private fun tokenToData(token: Token): String {

        return token.token

    }

    private fun tokenSharedPrefToDomain(valueReference: String): Token{

        return Token(token = valueReference)

    }
}
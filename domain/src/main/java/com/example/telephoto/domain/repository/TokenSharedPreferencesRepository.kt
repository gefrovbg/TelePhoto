package com.example.telephoto.domain.repository

import com.example.telephoto.domain.models.Token

interface TokenSharedPreferencesRepository {

    fun getTokenFromSharedPreferences(): Token?

    fun saveTokenToSharedPreferences(token: Token):Boolean

}
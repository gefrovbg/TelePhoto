package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Token
import java.lang.Exception

class SaveTokenToSharedPreferencesUseCase {

    private val getSharedPreferencesUseCase = GetSharedPreferencesUseCase()

    fun execute(token: Token):Boolean{
        return try {
            val shared = getSharedPreferencesUseCase.execute()
            val edit = shared.edit()
            edit.putString("token" , token.token)
            edit.apply()
            true
        }catch (e: Exception){
            false
        }
    }

}
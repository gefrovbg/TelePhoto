package com.example.telephoto.domain.repository

interface DescriptionSharedPreferencesRepository {

    fun getDescriptionFromSharedPreferences(): Boolean

    fun saveDescriptionToSharedPreferences(boolean: Boolean):Boolean

}
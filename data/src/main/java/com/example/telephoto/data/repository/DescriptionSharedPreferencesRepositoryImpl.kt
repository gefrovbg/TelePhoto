package com.example.telephoto.data.repository

import android.content.Context
import com.example.telephoto.data.storage.sharedprefs.SharedPreferenceRepositoryImpl
import com.example.telephoto.domain.repository.DescriptionSharedPreferencesRepository

class DescriptionSharedPreferencesRepositoryImpl(context: Context): DescriptionSharedPreferencesRepository {

    private val sharedPreferencesRepository = SharedPreferenceRepositoryImpl(context = context)

    override fun getDescriptionFromSharedPreferences(): Boolean {

        return sharedPreferencesRepository.getBooleanFromSharedPreferences(DESC_PREF_NAME)

    }

    override fun saveDescriptionToSharedPreferences(boolean: Boolean): Boolean {

        return sharedPreferencesRepository.saveBooleanFromSharedPreferences(DESC_PREF_NAME, boolean = boolean)

    }

    companion object{

        const val DESC_PREF_NAME = "description"

    }
}
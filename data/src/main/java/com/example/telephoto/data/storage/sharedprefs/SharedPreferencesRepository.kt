package com.example.telephoto.data.storage.sharedprefs

import android.content.SharedPreferences

interface SharedPreferencesRepository {

    fun getSharedSharedPreferences(): SharedPreferences

    fun getBooleanFromSharedPreferences(nameReference: String): Boolean

    fun saveBooleanFromSharedPreferences(nameReference: String, boolean: Boolean): Boolean

    fun getStringFromSharedPreferences(nameReference: String): String?

    fun saveStringFromSharedPreferences(nameReference: String, valueReference: String): Boolean

    companion object{

        const val SHAR_PREF_NAME = "TelegramBot"

    }

}
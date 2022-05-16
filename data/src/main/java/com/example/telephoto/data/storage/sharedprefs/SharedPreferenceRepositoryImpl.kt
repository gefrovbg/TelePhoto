package com.example.telephoto.data.storage.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.example.telephoto.data.storage.sharedprefs.SharedPreferencesRepository.Companion.SHAR_PREF_NAME
import java.lang.Exception

class SharedPreferenceRepositoryImpl(context: Context): SharedPreferencesRepository {

    val sharedPreferences = context.getSharedPreferences(SHAR_PREF_NAME, Context.MODE_PRIVATE)

    override fun getSharedSharedPreferences(): SharedPreferences{
        return sharedPreferences
    }

    override fun getBooleanFromSharedPreferences(nameReference: String): Boolean {
        return sharedPreferences.getBoolean(nameReference, false)
    }

    override fun saveBooleanFromSharedPreferences(nameReference: String, boolean: Boolean): Boolean {
        return try{
            val edit = sharedPreferences.edit()
            edit.putBoolean(nameReference, boolean)
            edit.apply()
            true
        }catch (e: Exception){
            false
        }
    }

    override fun getStringFromSharedPreferences(nameReference: String): String? {

        return sharedPreferences.getString(nameReference, "")

    }

    override fun saveStringFromSharedPreferences(nameReference: String, valueReference: String): Boolean {

        return try {
            val edit = sharedPreferences.edit()
            edit.putString(nameReference, valueReference)
            edit.apply()
            true
        }catch (e: Exception){
            false
        }

    }
}
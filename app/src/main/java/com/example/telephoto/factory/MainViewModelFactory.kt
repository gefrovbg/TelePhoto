package com.example.telephoto.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.telephoto.data.repository.DescriptionSharedPreferencesRepositoryImpl
import com.example.telephoto.domain.usecase.GetDescriptionFromSharedPreferencesUseCase
import com.example.telephoto.viewmodel.MainViewModel

class MainViewModelFactory(contextApp: Context) : ViewModelProvider.Factory {

    private val descriptionSharedPreferencesRepository by lazy { DescriptionSharedPreferencesRepositoryImpl(contextApp) }
    private val getDescriptionFromSharedPreferencesUseCase by lazy { GetDescriptionFromSharedPreferencesUseCase(descriptionSharedPreferencesRepository) }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getDescriptionFromSharedPreferencesUseCase = getDescriptionFromSharedPreferencesUseCase
        ) as T
    }
}
package com.example.telephoto.viewmodel

import androidx.lifecycle.ViewModel
import com.example.telephoto.domain.usecase.GetDescriptionFromSharedPreferencesUseCase

class MainViewModel(private val getDescriptionFromSharedPreferencesUseCase: GetDescriptionFromSharedPreferencesUseCase) : ViewModel() {

    val showDescriptionBoolean: Boolean = getDescriptionFromSharedPreferencesUseCase.execute()

}
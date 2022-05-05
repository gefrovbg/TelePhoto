package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.repository.DescriptionSharedPreferencesRepository

class GetDescriptionFromSharedPreferencesUseCase(private val descriptionSharedPreferencesRepository: DescriptionSharedPreferencesRepository) {

    fun execute(): Boolean{
        return descriptionSharedPreferencesRepository.getDescriptionFromSharedPreferences()
    }

}
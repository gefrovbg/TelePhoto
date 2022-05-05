package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.repository.DescriptionSharedPreferencesRepository

class SaveDescriptionToSharedPreferencesUseCase(private val descriptionSharedPreferencesRepository: DescriptionSharedPreferencesRepository) {

    fun execute(boolean: Boolean): Boolean{
        return descriptionSharedPreferencesRepository.saveDescriptionToSharedPreferences(boolean)
    }

}
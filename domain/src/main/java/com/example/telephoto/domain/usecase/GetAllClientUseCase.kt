package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.repository.DataBaseRepository

class GetAllClientUseCase(private val dataBaseRepository: DataBaseRepository) {

    fun execute(): ArrayList<Client>{
        return dataBaseRepository.getAll()
    }

}
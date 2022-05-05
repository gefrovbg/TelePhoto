package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.repository.DataBaseRepository

class GetClientByNicknameUseCase(private val dataBaseRepository: DataBaseRepository) {

    fun execute(client: Client): Client?{
        return dataBaseRepository.getClientByNickname(client = client)
    }

}
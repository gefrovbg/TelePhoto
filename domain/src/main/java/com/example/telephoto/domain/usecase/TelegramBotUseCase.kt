package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.repository.TelegramBotRepository

class TelegramBotUseCase(private val telegramBotRepository: TelegramBotRepository) {

    fun start(){
        telegramBotRepository.telegramBotStart()
    }

    fun stop(){
        telegramBotRepository.telegramBotStop()
    }

}
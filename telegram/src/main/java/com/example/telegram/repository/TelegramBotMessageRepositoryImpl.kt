package com.example.telegram.repository

import com.example.telephoto.domain.models.Client
//import com.example.telephoto.presentation.adapters.ClientAdapter
import java.io.File

class TelegramBotMessageRepositoryImpl(
    private val telegramBotMessageOnCommandAddRepository: TelegramBotMessageOnCommandAddRepository,
    private val telegramBotMessageOnCommandPhotoRepository: TelegramBotMessageOnCommandPhotoRepository
): TelegramBotMessageRepository {

    override fun onCommandPhoto(client: Client, successCallback: (File) -> Unit, errorCallback: (String) -> Unit) {

        telegramBotMessageOnCommandPhotoRepository.onCommandPhoto(client,
            {
                successCallback(it)
            },{
                errorCallback(it)
            }
        )

    }

    override fun onCommandAdd(client: Client): String {

        return telegramBotMessageOnCommandAddRepository.noCommandAdd(client)

    }

}
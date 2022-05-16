package com.example.telegram.repository

import com.example.telephoto.domain.models.Client

interface TelegramBotMessageOnCommandAddRepository {

    fun noCommandAdd(client: Client): String

}
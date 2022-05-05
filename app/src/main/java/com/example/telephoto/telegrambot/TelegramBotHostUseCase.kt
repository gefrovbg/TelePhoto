package com.example.telephoto.telegrambot

import com.elbekD.bot.Bot
import com.example.telephoto.domain.models.Token

class TelegramBotHostUseCase {

    fun execute(token: Token): Bot {
        return Bot.createPolling("", token.token)
    }

}
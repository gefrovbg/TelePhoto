package com.example.telephoto.telegrambot.repository

import androidx.camera.core.ImageCapture
import com.elbekD.bot.Bot
import com.example.telephoto.domain.models.Client
import com.example.telephoto.presentation.adapters.ClientAdapter

class TelegramBotRepositoryImpl: TelegramBotRepository {

    private val telegramBotMessageRepository by lazy { TelegramBotMessageRepositoryImpl() }

    override fun execute(bot: Bot, imageCapture: ImageCapture?, adapter: ClientAdapter?) {

        bot.onCommand("/photo") { msg, _ ->
            telegramBotMessageRepository.onCommandPhoto(
                Client(
                    chatId = msg.chat.id,
                    firstName = msg.chat.first_name,
                    lastName = msg.chat.last_name,
                    nickname = msg.chat.username
                ), imageCapture = imageCapture, {
                    bot.sendPhoto(msg.chat.id, it)
                }, {
                    bot.sendMessage(msg.chat.id, it)
                })
        }

        bot.onCommand("/add") { msg, _ ->

            bot.sendMessage(
                msg.chat.id, telegramBotMessageRepository.onCommandAdd(
                    Client(
                        chatId = msg.chat.id,
                        firstName = msg.chat.first_name,
                        lastName = msg.chat.last_name,
                        nickname = msg.chat.username
                    ),
                    adapter = adapter
                )
            )

        }
    }
}
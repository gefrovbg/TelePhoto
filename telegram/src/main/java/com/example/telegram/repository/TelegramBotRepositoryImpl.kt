package com.example.telegram.repository

import androidx.lifecycle.MutableLiveData
import com.elbekD.bot.Bot
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.repository.TelegramBotRepository
import com.example.telephoto.domain.usecase.GetTokenFromSharedPreferencesUseCase

class TelegramBotRepositoryImpl(
    private val tokenSharedPreferencesRepository: TokenSharedPreferencesRepositoryImpl,
    private val telegramBotMessageOnCommandAddRepository: TelegramBotMessageOnCommandAddRepository,
    private val telegramBotMessageOnCommandPhotoRepository: TelegramBotMessageOnCommandPhotoRepository
): TelegramBotRepository {

    private val getTokenFromSharedPreferencesUseCase by lazy { GetTokenFromSharedPreferencesUseCase(tokenSharedPreferencesRepository) }
    private val telegramBotMessageRepository by lazy { TelegramBotMessageRepositoryImpl(
        telegramBotMessageOnCommandAddRepository = telegramBotMessageOnCommandAddRepository,
        telegramBotMessageOnCommandPhotoRepository = telegramBotMessageOnCommandPhotoRepository
    ) }

    override fun telegramBotStart() {

        val token = getTokenFromSharedPreferencesUseCase.execute()
        if (token != null){
            if (token.token != "") bot.value = Bot.createPolling("", token.token)
        }

        bot.value?.onCommand("/photo") { msg, _ ->
            telegramBotMessageRepository.onCommandPhoto(
                Client(
                    chatId = msg.chat.id,
                    firstName = msg.chat.first_name,
                    lastName = msg.chat.last_name,
                    nickname = msg.chat.username
                ), {
                    bot.value?.sendPhoto(msg.chat.id, it)
                }, {
                    bot.value?.sendMessage(msg.chat.id, it)
                })
        }

        bot.value?.onCommand("/add") { msg, _ ->

            bot.value?.sendMessage(
                msg.chat.id, telegramBotMessageRepository.onCommandAdd(
                    Client(
                        chatId = msg.chat.id,
                        firstName = msg.chat.first_name,
                        lastName = msg.chat.last_name,
                        nickname = msg.chat.username
                    )
                )
            )

        }
        bot.value?.start()

    }

    private companion object{

        val bot = MutableLiveData<Bot>()

    }

    override fun telegramBotStop() {
        bot.value?.stop()
    }
}
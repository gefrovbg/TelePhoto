package com.example.telephoto.telegrambot.repository

import androidx.camera.core.ImageCapture
import com.elbekD.bot.Bot
import com.example.telephoto.presentation.adapters.ClientAdapter

interface TelegramBotRepository {

    fun execute(bot: Bot, imageCapture: ImageCapture?, adapter: ClientAdapter?)

}
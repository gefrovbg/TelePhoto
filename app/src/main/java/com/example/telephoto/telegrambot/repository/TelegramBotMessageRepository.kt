package com.example.telephoto.telegrambot.repository

import androidx.camera.core.ImageCapture
import com.example.telephoto.domain.models.Client
import com.example.telephoto.presentation.adapters.ClientAdapter
import java.io.File

interface TelegramBotMessageRepository {

    fun onCommandPhoto(client: Client, imageCapture: ImageCapture?, successCallback: (File) -> Unit, errorCallback: (String) -> Unit)

    fun onCommandAdd(client: Client, adapter: ClientAdapter?): String

}
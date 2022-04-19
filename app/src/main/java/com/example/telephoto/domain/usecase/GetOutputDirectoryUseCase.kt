package com.example.telephoto.domain.usecase

import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import java.io.File

class GetOutputDirectoryUseCase {

    private val contextApp = TelegramBotApp.context

    fun execute(): File {
        val mediaDir = contextApp.externalMediaDirs.firstOrNull()?.let {
            File(it, contextApp.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else contextApp.filesDir
    }

}
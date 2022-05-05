package com.example.telephoto.data.storage.filesystem.usecase

import android.content.Context
import java.io.File

class GetOutputDirectoryUseCase {

    fun execute(contextApp: Context): File {
        val mediaDir = contextApp.externalMediaDirs.firstOrNull()?.let {
            File(it, "TelePhoto").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else contextApp.filesDir
    }

}
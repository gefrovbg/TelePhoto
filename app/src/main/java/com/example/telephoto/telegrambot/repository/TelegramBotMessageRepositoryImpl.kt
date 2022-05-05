package com.example.telephoto.telegrambot.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.repository.DataBaseRepositoryImpl
import com.example.telephoto.data.storage.client.models.ClientSQLite
import com.example.telephoto.data.storage.client.sqlite.SQLiteRepositoryImpl
import com.example.telephoto.data.storage.filesystem.usecase.GetOutputDirectoryUseCase
import com.example.telephoto.domain.models.Client
import com.example.telephoto.presentation.adapters.ClientAdapter
import java.io.File

class TelegramBotMessageRepositoryImpl: TelegramBotMessageRepository {

    private val contextApp = TelegramBotApp.context
    private val sqliteRepository by lazy{ SQLiteRepositoryImpl(context = contextApp, factory = null) }
    private val getOutputDirectory = GetOutputDirectoryUseCase()
    private val dataBaseRepository by lazy { DataBaseRepositoryImpl(contextApp, null) }

    override fun onCommandPhoto(client: Client, imageCapture: ImageCapture?, successCallback: (File) -> Unit, errorCallback: (String) -> Unit) {

        val clientSQLite = dataBaseRepository.clientToData(client)
        val getClient = sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}"))
        if ( getClient != null) {
            if (!getClient.addStatus!!){
                try {
                    val outputDirectory  = getOutputDirectory.execute(contextApp)
                    val photoFile = File(outputDirectory, "photo.jpg")
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    Handler(Looper.getMainLooper()).post {
                        imageCapture?.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(contextApp),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onError(exc: ImageCaptureException) {
                                    errorCallback("Photo capture failed: ${exc.message}")
                                    Log.e(
                                        "Bot",
                                        "Photo capture failed: ${exc.message}",
                                        exc
                                    )
                                }

                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    successCallback(photoFile)
                                }
                            }
                        )
                    }
                }catch (e: java.lang.Exception){
                    Log.i("Bot", e.message.toString())
                }
            }else{
                errorCallback("Wait for the administrator to add you!")
            }
        }else{
            errorCallback("It`s private chat!")
        }

    }

    override fun onCommandAdd(client: Client, adapter: ClientAdapter?): String {

        val clientSQLite = dataBaseRepository.clientToData(client)

        if (sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}")) == null){
            return if (sqliteRepository.addClient(ClientSQLite(clientSQLite.chatId, clientSQLite.firstName ,clientSQLite.lastName, "@${clientSQLite.nickname}", true))){
                val getClient = sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}"))
                if (getClient != null){
                    Handler(Looper.getMainLooper()).post {
                        adapter?.insertItem(dataBaseRepository.clientToDomain(getClient))
                    }
                    "Your request added!"
                }else{
                    "Repeat later!"
                }
            }else
                "Repeat later!"
        }else{
            return if(sqliteRepository.getClientByNickname(ClientSQLite(nickname = "@${clientSQLite.nickname}"))!!.addStatus == true)
                "Your request has not yet been processed!"
            else
                "You`re already added!"
        }
    }

}
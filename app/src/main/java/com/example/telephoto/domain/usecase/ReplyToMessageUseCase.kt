package com.example.telephoto.domain.usecase

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.elbekD.bot.Bot
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.domain.models.ChatId
import com.example.telephoto.presentation.adapters.ClientAdapter
import java.io.File

class ReplyToMessageUseCase {

    private val contextApp = TelegramBotApp.context
    private val getOutputDirectory = GetOutputDirectoryUseCase()
    private val database = DataBaseHelperUseCase(contextApp, null)

    fun execute(bot: Bot, adapter: ClientAdapter? = null, imageCapture: ImageCapture? = null){

        bot.onCommand("/photo") { msg, _ ->
            Handler(Looper.getMainLooper()).post {
                val client = database.getClientByNickname("@${msg.chat.username.toString()}")
                if ( client != null) {
                    if (!client.addStatus){
                        try {
                            val outputDirectory  = getOutputDirectory.execute()
                            val photoFile = File(outputDirectory, "photo.jpg")
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                            imageCapture?.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(contextApp),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onError(exc: ImageCaptureException) {
                                        bot.sendMessage(msg.chat.id, "Photo capture failed: ${exc.message}")
                                        Log.e(
                                            "Bot",
                                            "Photo capture failed: ${exc.message}",
                                            exc
                                        )
                                    }
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        bot.sendPhoto(msg.chat.id, photoFile)
                                    }
                                })
                        }catch (e: java.lang.Exception){
                            Log.i("Bot", e.message.toString())
                        }
                    }
                }else{
                    bot.sendMessage(msg.chat.id, "It`s private chat!")
                }
            }
        }

        bot.onCommand("/add") { msg, _ ->
            Handler(Looper.getMainLooper()).post {
                if ( database.getClientByNickname("@${msg.chat.username.toString()}") == null){
                    if (database.addClient(ChatId(msg.chat.id, msg.chat.first_name.toString(),msg.chat.last_name.toString(), "@${msg.chat.username.toString()}", true))){
                        database.getClientByNickname("@${msg.chat.username.toString()}")
                            ?.let {
                                Toast.makeText(contextApp, "New client!", Toast.LENGTH_SHORT).show()
                                adapter?.insertItem(it)
                            }
                    }
                }else{
                    if(database.getClientByNickname("@${msg.chat.username.toString()}")!!.addStatus)
                        bot.sendMessage(msg.chat.id, "Your request has not yet been processed!")
                    else
                        bot.sendMessage(msg.chat.id, "You`re already added!")
                }
            }
        }
    }

}
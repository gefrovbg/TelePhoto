package com.example.telephoto.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.repository.DescriptionSharedPreferencesRepositoryImpl
import com.example.telephoto.domain.usecase.SaveDescriptionToSharedPreferencesUseCase

class ShowCustomDialogUseCase {

    private val contextApp = TelegramBotApp.context
    private val descriptionSharedPreferencesRepository by lazy { DescriptionSharedPreferencesRepositoryImpl(contextApp) }
    private val saveDescriptionToSharedPreferencesUseCase by lazy { SaveDescriptionToSharedPreferencesUseCase(descriptionSharedPreferencesRepository) }

    fun execute(view: View, textForDialog: Int, context: Context){

        val alertDialog = AlertDialog.Builder(context)
        val builder = alertDialog
            .create()
        val buttonOk = view.findViewById<Button>(R.id.btn_ok)
        val text = view.findViewById<TextView>(R.id.text)
        text.setText(textForDialog)
        builder.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setView(view)
            setCancelable(false)
        }.show()
        builder.setView(view)
        buttonOk.setOnClickListener {
            if (saveDescriptionToSharedPreferencesUseCase.execute(boolean = true)) builder.dismiss()
            else {
                Toast.makeText(contextApp, "Someone wrong!", Toast.LENGTH_SHORT).show()
                builder.dismiss()
            }
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

}
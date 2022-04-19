package com.example.telephoto.domain.usecase

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.telephoto.R

class ShowCustomDialogUseCase {

    private val getSharedPreferencesUseCase = GetSharedPreferencesUseCase()

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
            val shared = getSharedPreferencesUseCase.execute()
            val edit = shared.edit()
            edit.putBoolean("description", true)
            edit.apply()
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

}
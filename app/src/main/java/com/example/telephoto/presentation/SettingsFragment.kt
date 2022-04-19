package com.example.telephoto.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elbekD.bot.Bot
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.databinding.FragmentSettingsBinding
import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.usecase.*
import com.example.telephoto.presentation.adapters.ClientAdapter
import java.lang.Exception

class SettingsFragment : Fragment() {

    private val telegramBotHostUseCase = TelegramBotHostUseCase()
    private lateinit var bot: Bot
    private val getTokenFromSharedPreferencesUseCase = GetTokenFromSharedPreferencesUseCase()
    private val saveTokenToSharedPreferencesUseCase = SaveTokenToSharedPreferencesUseCase()
    private val listChatId = TelegramBotApp.listChatId
    private val adapter = ClientAdapter(listChatId)
    private val contextApp = TelegramBotApp.context
    private val replyToMessageUseCase = ReplyToMessageUseCase()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tokenEditText.setText(getTokenFromSharedPreferencesUseCase.execute()?.token)
        val warning = binding.warning
        val tokenEditText = binding.tokenEditText
        tokenEditText.doAfterTextChanged { warning.visibility = View.GONE }

        binding.btnOk.setOnClickListener {
            if (tokenEditText.text.toString() == ""){
                warning.visibility = View.VISIBLE
            }else{
                val token = Token(binding.tokenEditText.text.toString())
                try {
                    bot.stop()
                }catch (e: Exception){
                    Log.i("Bot", "${e.message}")
                }
                if(!saveTokenToSharedPreferencesUseCase.execute(token)) Toast.makeText(contextApp, "Sorry, token don`t save!", Toast.LENGTH_SHORT).show()
                getTokenFromSharedPreferencesUseCase.execute()?.let {
                    bot = telegramBotHostUseCase.execute(it)
                    replyToMessageUseCase.execute(bot, adapter)
                    try {
                        bot.start()
                    }catch (e: Exception){
                        Toast.makeText(contextApp, "Check token!", Toast.LENGTH_SHORT).show()
                        Log.i("Bot", "${e.message}")
                    }
                }
            }
        }

        val database = DataBaseHelperUseCase(contextApp, null)
        val allClient = database.getAll()
        listChatId.clear()
        listChatId.addAll(allClient)
        val recyclerView = binding.clientRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(contextApp)
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            if (it.token != ""){
                bot = telegramBotHostUseCase.execute(it)
                replyToMessageUseCase.execute(bot, adapter)
                try {
                    bot.start()
                }catch (e: Exception){
                    Toast.makeText(contextApp, "Check token!", Toast.LENGTH_SHORT).show()
                    Log.i("Bot", "${e.message}")
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            if (it.token != "") {
                bot.stop()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
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
import com.example.telephoto.data.repository.DataBaseRepositoryImpl
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.databinding.FragmentSettingsBinding
import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.usecase.*
import com.example.telephoto.presentation.adapters.ClientAdapter
import com.example.telephoto.telegrambot.TelegramBotHostUseCase
import com.example.telephoto.telegrambot.repository.TelegramBotRepositoryImpl

class SettingsFragment : Fragment() {

    private val tokenSharedPreferencesRepository by lazy { TokenSharedPreferencesRepositoryImpl(contextApp) }
    private val getTokenFromSharedPreferencesUseCase by lazy { GetTokenFromSharedPreferencesUseCase(tokenSharedPreferencesRepository)}
    private val saveTokenToSharedPreferencesUseCase by lazy { SaveTokenToSharedPreferencesUseCase(tokenSharedPreferencesRepository)}
    private val telegramBotHostUseCase = TelegramBotHostUseCase()
    private val contextApp = TelegramBotApp.context
    private val dataBaseRepository by lazy { DataBaseRepositoryImpl(contextApp, null) }
    private val getAllClientUseCase by lazy { GetAllClientUseCase(dataBaseRepository) }
    private val telegramBotRepository by lazy { TelegramBotRepositoryImpl() }
    private lateinit var bot: Bot

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
        val allClient = getAllClientUseCase.execute()
        val adapter = ClientAdapter(allClient)

        val recyclerView = binding.clientRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(contextApp)
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            if (it.token != ""){
                bot = telegramBotHostUseCase.execute(it)
                telegramBotRepository.execute(bot, null, adapter)
                try {
                    bot.start()
                }catch (e: Exception){
                    Toast.makeText(contextApp, "Check token!", Toast.LENGTH_SHORT).show()
                    Log.i("Bot", "${e.message}")
                }
            }
        }

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
                    telegramBotRepository.execute(bot, null, adapter)
                    try {
                        bot.start()
                    }catch (e: Exception){
                        Toast.makeText(contextApp, "Check token!", Toast.LENGTH_SHORT).show()
                        Log.i("Bot", "${e.message}")
                    }
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
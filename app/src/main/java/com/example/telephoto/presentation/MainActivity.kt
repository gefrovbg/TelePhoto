package com.example.telephoto.presentation

import TelegramBotMessageRepositoryAppImpl
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.telegram.repository.TelegramBotRepositoryImpl
import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.databinding.ActivityMainBinding
import com.example.telephoto.domain.usecase.TelegramBotUseCase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val contextApp = TelegramBotApp.context
    private val tokenSharedPreferencesRepository by lazy { TokenSharedPreferencesRepositoryImpl(contextApp) }
    private val telegramBotRepository by lazy { TelegramBotRepositoryImpl(
        tokenSharedPreferencesRepository = tokenSharedPreferencesRepository,
        telegramBotMessageOnCommandPhotoRepository = TelegramBotMessageRepositoryAppImpl(),
        telegramBotMessageOnCommandAddRepository = TelegramBotMessageRepositoryAppImpl()
    ) }
    private val telegramBotUseCase by lazy { TelegramBotUseCase(telegramBotRepository)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)

        if (!allPermissionsGranted()) ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        telegramBotUseCase.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        telegramBotUseCase.stop()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

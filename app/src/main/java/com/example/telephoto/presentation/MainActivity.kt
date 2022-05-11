package com.example.telephoto.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.databinding.ActivityMainBinding
import com.example.telephoto.factory.MainViewModelFactory
import com.example.telephoto.presentation.usecase.ShowCustomDialogUseCase
import com.example.telephoto.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val contextApp = TelegramBotApp.context
    private lateinit var binding: ActivityMainBinding
    private val showCustomDialogUseCase = ShowCustomDialogUseCase()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(contextApp = contextApp))[MainViewModel::class.java]

        val bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)

        if (!allPermissionsGranted()) ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        if (!viewModel.showDescriptionBoolean) {
            val view = layoutInflater.inflate(R.layout.dialog_for_description, null)
            val string = R.string.description
            showCustomDialogUseCase.execute(view, string, this)
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

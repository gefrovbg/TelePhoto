package com.example.telephoto.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.elbekD.bot.Bot
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.databinding.FragmentPhotoBinding
import com.example.telephoto.domain.usecase.GetTokenFromSharedPreferencesUseCase
import com.example.telephoto.telegrambot.TelegramBotHostUseCase
import com.example.telephoto.telegrambot.repository.TelegramBotRepositoryImpl

class PhotoFragment : Fragment() {

    private val tokenSharedPreferencesRepository by lazy { TokenSharedPreferencesRepositoryImpl(contextApp) }
    private var imageCapture: ImageCapture? = null
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private val permissionBoolean = MutableLiveData(true)
    private val telegramBotRepository by lazy { TelegramBotRepositoryImpl() }
    private val telegramBotHostUseCase = TelegramBotHostUseCase()
    private val contextApp = TelegramBotApp.context
    private val getTokenFromSharedPreferencesUseCase by lazy { GetTokenFromSharedPreferencesUseCase(tokenSharedPreferencesRepository) }
    private lateinit var bot: Bot

    private val orientationEventListener by lazy {
        object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture!!.targetRotation = rotation
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            permissionBoolean.value = isGranted
        }
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (permissionBoolean.value == true) startCamera()
    }


    override fun onPause() {
        super.onPause()
        getTokenFromSharedPreferencesUseCase.execute()?.let {
            if (it.token != "") {
                bot.stop()
            }
        }
        orientationEventListener.disable()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture
                .Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            orientationEventListener.enable()
            getTokenFromSharedPreferencesUseCase.execute()?.let {
                if (it.token != ""){
                    bot = telegramBotHostUseCase.execute(it)
                    telegramBotRepository.execute(bot, imageCapture = imageCapture, null)
                    try {
                        bot.start()
                    }catch (e: Exception){
                        Toast.makeText(contextApp, "Check token!", Toast.LENGTH_SHORT).show()
                        Log.i("Bot", "${e.message}")
                    }
                }
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("camera", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
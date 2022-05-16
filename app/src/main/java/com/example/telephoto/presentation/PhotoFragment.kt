package com.example.telephoto.presentation

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.data.storage.filesystem.usecase.GetOutputDirectoryUseCase
import com.example.telephoto.databinding.FragmentPhotoBinding
import java.io.File

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private val permissionBoolean = MutableLiveData(true)
    private val contextApp = TelegramBotApp.context
    private val getOutputDirectory = GetOutputDirectoryUseCase()


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
        boolean.value = true
        if (permissionBoolean.value == true) startCamera()
    }


    override fun onPause() {
        super.onPause()
        boolean.value = false
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

    fun takePhoto(successCallback: (File) -> Unit, errorCallback: (String) -> Unit) {
        val outputDirectory  = getOutputDirectory.execute(contextApp)
        val photoFile = File(outputDirectory, "photo.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        if (boolean.value == true){
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
        }else{
            errorCallback("Open camera tab!")
        }

    }

    companion object{
        var imageCapture: ImageCapture? = null
        val boolean = MutableLiveData<Boolean>()
    }

}
package com.android04.godfisherman.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android04.godfisherman.R
import com.android04.godfisherman.databinding.ActivityCameraBinding
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.camera.upload.UploadActivity
import com.android04.godfisherman.utils.ObjectDetector
import com.android04.godfisherman.utils.toByteArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : BaseActivity<ActivityCameraBinding, CameraViewModel>(R.layout.activity_camera),
    SensorEventListener {
    override val viewModel: CameraViewModel by viewModels()

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private val screenSize : Size by lazy {
        Size(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    private val sensorManager by lazy{
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFullScreen()
        setBinding()
        operateCamera()
    }

    private fun setFullScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun setBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun operateCamera() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(screenSize)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(screenSize)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, FishAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this, getText(R.string.camera_start_error), Toast.LENGTH_SHORT
                ).show()
                finish()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    getText(R.string.camera_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    fun takePhoto() {
        val toastError = Toast.makeText(
            this@CameraActivity, R.string.camera_capture_error, Toast.LENGTH_SHORT
        )

        val toastSuccess = Toast.makeText(
            this@CameraActivity, viewModel.bodySize.value.toString(), Toast.LENGTH_SHORT
        )

        val intent = Intent(this, UploadActivity::class.java)

        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    toastError.show()
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    val buffer = image.planes[0].buffer
                    val data = buffer.toByteArray()

                    toastSuccess.show()

                    startActivity(intent)
                    image.close()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let{
            binding.lvTest.onSensorEvent(event)
            viewModel.changedLevel(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        sensorManager.unregisterListener(this)
    }

    private inner class FishAnalyzer : ImageAnalysis.Analyzer {

        val detector = ObjectDetector()

        override fun analyze(image: ImageProxy) {
            detector.detectImage(image) { rectList ->
                viewModel.setRect(
                    rectList.map {
                        listOf(heightConvert(it.top, image.height),
                            heightConvert(it.bottom, image.height),
                            widthConvert(it.left, image.width),
                            widthConvert(it.right, image.width))
                    }
                )
                viewModel.setSize(
                    rectList.map {
                        it.right - it.left
                    }
                )
            }
        }

    }

    fun widthConvert(x: Int, imageWidth: Int) : Int {
        val width = screenSize.width.toFloat()
        val ratio = width / imageWidth

        return (x * ratio).toInt()
    }

    fun heightConvert(y: Int, imageHeight: Int) : Int {
        val height = screenSize.height.toFloat()
        val ratio = height / imageHeight

        return (y * ratio).toInt()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
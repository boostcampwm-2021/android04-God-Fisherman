package com.android04.godfisherman.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android04.godfisherman.R
import com.android04.godfisherman.common.App
import com.android04.godfisherman.databinding.ActivityCameraBinding
import com.android04.godfisherman.presentation.camera.CameraViewModel
import com.android04.godfisherman.ui.base.BaseActivity
import com.android04.godfisherman.ui.upload.UploadActivity
import com.android04.godfisherman.utils.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : BaseActivity<ActivityCameraBinding, CameraViewModel>(R.layout.activity_camera),
    SensorEventListener {
    override val viewModel: CameraViewModel by viewModels()

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private val screenSize: Size by lazy {
        Size(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    private val sensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFullScreen()
        initBinding()
        operateCamera()
        (application as App).exitCameraActivityFlag = true
    }

    private fun initFullScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun initBinding() {
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
                showToast(this, R.string.camera_start_error)
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
                showToast(this, R.string.camera_permission_denied)
                finish()
            }
        }
    }

    fun takePhoto() {
        viewModel.setShutterPressed(true)

        val intent = Intent(this, UploadActivity::class.java)

        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    showToast(this@CameraActivity, R.string.camera_capture_error)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    val buffer = image.planes[0].buffer
                    val data = buffer.toByteArray()
                    val origin = BitmapFactory.decodeByteArray(data, 0, data.size)

                    val rect = viewModel.getCropRect(screenSize.width, screenSize.height, image.width, image.height)
                    val size = viewModel.bodySize.value

                    if (rect != null && size != null) {
                        val crop = Bitmap.createBitmap(origin, rect[2], rect[0], rect[3] - rect[2], rect[1] - rect[0])

                        captureImage = crop
                        intent.putExtra(INTENT_FISH_SIZE, size)
                        startActivity(intent)

                        showToast(this@CameraActivity, R.string.camera_capture_success)
                        image.close()

                        finish()
                    } else {
                        viewModel.setShutterPressed(false)
                        showToast(this@CameraActivity, R.string.camera_detect_error)
                        image.close()
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            binding.lvTest.onSensorEvent(event)

            val x = event.values[0]
            val y = event.values[1]

            viewModel.changedLevel(x, y)
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
                        listOf(
                            heightConvert(it.top, image.height, screenSize.height),
                            heightConvert(it.bottom, image.height, screenSize.height),
                            widthConvert(it.left, image.width, screenSize.width),
                            widthConvert(it.right, image.width, screenSize.width)
                        )
                    }
                )
            }
        }
    }

    companion object {
        const val INTENT_FISH_SIZE = "Fish Size"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        var captureImage: Bitmap? = null
    }
}

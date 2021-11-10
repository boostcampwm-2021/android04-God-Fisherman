package com.android04.godfisherman.utils

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetector {
    private val options =  ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableMultipleObjects()
        .enableClassification()
        .build()

    private val detector = ObjectDetection.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    fun detectImage(imageProxy: ImageProxy, successCallback: (List<Rect>) -> Unit) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val process = detector.process(image)

            process.addOnSuccessListener {
                successCallback(it.map {
                    it.boundingBox
                })
            }

            process.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }
}
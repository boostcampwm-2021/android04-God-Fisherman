package com.android04.godfisherman.data.datasource.uploadDataSource.remote

import android.graphics.Bitmap
import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.android04.godfisherman.data.entity.FishingRecord
import com.android04.godfisherman.data.entity.TypeInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UploadRemoteDataSourceImpl @Inject constructor() : UploadDataSource.RemoteDataSource {
    private val database = Firebase.firestore
    private val storage = Firebase.storage

    override suspend fun fetchFishTypeList(): List<String> {
        val fishTypeList = mutableListOf<String>()
        database.collection("Data").document("species")
            .get()
            .addOnSuccessListener {
                with(fishTypeList) { addAll(it.data?.get("array") as List<String>) }
            }
            .addOnFailureListener { throw it }
            .await()

        return fishTypeList
    }

    override suspend fun getImageUrl(bitmap: Bitmap): String? {
        var imageUrl: String? = null

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_${timeStamp}.jpg"

        val imagesRef = storage.reference.child("images").child(imageFileName)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imagesRef.downloadUrl
        }.addOnSuccessListener {
            imageUrl = it.toString()
        }.addOnFailureListener {
            throw it
        }.await()

        return imageUrl
    }

    override suspend fun saveImageType(typeInfo: TypeInfo, fishingRecord: FishingRecord) {
        val newImageTypeRef = database.collection("Feed")

        newImageTypeRef.add(typeInfo).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }

            task.result.collection("fishingRecord").add(fishingRecord)
        }.addOnFailureListener {
            throw it
        }
    }

    override suspend fun saveTimeLineType(
        typeInfo: TypeInfo,
        fishingRecordList: List<FishingRecord>
    ): Boolean {
        val newTimeLineTypeRef = database.collection("Feed")

        try {
            val result = newTimeLineTypeRef.add(typeInfo).await()

            result.collection("fishingRecord").run {
                fishingRecordList.forEach { record ->
                    add(record).await()
                }
            }

        } catch (e: Exception) {
            return false
        }

        return true
    }
}
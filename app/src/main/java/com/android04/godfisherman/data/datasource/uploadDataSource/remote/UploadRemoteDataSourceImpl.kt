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
        database.collection(FISH_TYPE_COLLECTION_NAME).document(FISH_TYPE_DOCUMENT_NAME)
            .get()
            .addOnSuccessListener {
                with(fishTypeList) { addAll(it.data?.get(FISH_TYPE_KEY) as List<String>) }
            }
            .addOnFailureListener { throw it }
            .await()

        return fishTypeList
    }

    override suspend fun getImageUrl(bitmap: Bitmap): String? {
        var imageUrl: String? = null

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_${timeStamp}.jpg"

        val imagesRef = storage.reference.child(STORAGE_IMAGE_PATH).child(imageFileName)

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
        val newImageTypeRef = database.collection(FEED_COLLECTION_NAME)

        newImageTypeRef.add(typeInfo).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }

            task.result.collection(FISHING_RECORD_COLLECTION_NAME).add(fishingRecord)
        }.addOnFailureListener {
            throw it
        }
    }

    override suspend fun saveTimeLineType(
        typeInfo: TypeInfo,
        fishingRecordList: List<FishingRecord>
    ): Boolean {
        val newTimeLineTypeRef = database.collection(FEED_COLLECTION_NAME)

        try {
            val result = newTimeLineTypeRef.add(typeInfo).await()

            result.collection(FISHING_RECORD_COLLECTION_NAME).run {
                fishingRecordList.forEach { record ->
                    add(record).await()
                }
            }

        } catch (e: Exception) {
            return false
        }

        return true
    }

    companion object {
        const val FISH_TYPE_COLLECTION_NAME = "Data"
        const val FISH_TYPE_DOCUMENT_NAME = "species"
        const val FISH_TYPE_KEY = "array"
        const val STORAGE_IMAGE_PATH = "images"
        const val FEED_COLLECTION_NAME = "Feed"
        const val FISHING_RECORD_COLLECTION_NAME = "fishingRecord"
    }
}
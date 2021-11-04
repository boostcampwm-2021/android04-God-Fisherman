package com.android04.godfisherman.data.datasource.uploadDataSource.remote

import com.android04.godfisherman.data.datasource.uploadDataSource.UploadDataSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UploadRemoteDataSourceImpl @Inject constructor() : UploadDataSource.RemoteDataSource {
    private var database = Firebase.firestore

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
}
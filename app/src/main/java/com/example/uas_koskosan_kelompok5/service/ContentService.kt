package com.example.uas_koskosan_kelompok5.service

import android.content.ContentResolver
import android.net.Uri
import com.example.uas_koskosan_kelompok5.dao.ContentDao
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.ContentModel
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.model.TransactionData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class ContentService(firebase: FirebaseRealtimeService, firebaseStorage: FirebaseStorageService): ContentDao {
    private val database = firebase.getReferenceChild("content")
    private val storage = firebaseStorage
    override suspend fun crateContent(
        userid: String?,
        model: ContentModel,
        images: List<Uri>,
        contextResolver: ContentResolver
    ): ContentData {
        try {
            val ref = database.push()
            Log.d("IMAGES",images.toString())
            if(images.isNotEmpty()){
                val imagesUrls = images.map { uri ->
                    val image = storage.readImageBytes(contentResolver = contextResolver, uri = uri)
                    Log.d("IMAGES", image.toString())
                    storage.uploadImageToFirebase(image)
                }
                Log.d("IMAGES", imagesUrls.toString())
                val data = model.copy(
                    id = ref.key,
                    userId = userid,
                    images = imagesUrls.filterNotNull()
                )
                Log.d("IMAGES", data.toString())
                ref.setValue(data).await()
                return ContentData(data = data)
            }else {
                val data = model.copy(
                    id = ref.key,
                    userId = userid,
                    images = listOf("https://firebasestorage.googleapis.com/v0/b/uas-koskosan-kelompok5.appspot.com/o/images%2F8bacd7dd-47bd-4370-ae5d-bd0f7fee6ea7.jpg?alt=media&token=73b3103c-ac42-4052-af36-bfeae27f9e7e")
                )
                ref.setValue(data).await()
                return ContentData(data = data)
            }
        }catch (e: Exception){
            Log.e("CONTENT", e.message.toString())
            return ContentData(data = null, errorMessage = e.message.toString())
        }
    }


    override fun getData(data: MutableState<List<ContentModel>>) {

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<ContentModel>()

                for (childSnapshot in snapshot.children) {
                    try {
                        val myData = childSnapshot.getValue(ContentModel::class.java)
                        if (myData != null) {
                            dataList.add(myData)
                        }
                    } catch (e: Exception) {
                        Log.e("DATAHOME", "Error occurred: ${e.message}")
                    }
                }

                data.value = dataList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if data fetching is canceled
            }
        })
    }

    override suspend fun getContentById(id: String): ContentData {
        return try {
            val dataSnapshot = database.child(id).get().await()
            val result = dataSnapshot.getValue(ContentModel::class.java)
            ContentData(data = result, errorMessage = result?.let { null } ?: "Id Not Found")
        } catch (e: Exception) {
            ContentData(data = null, errorMessage = e.message)
        }
    }
    override suspend fun updateContent(id: String, content: ContentModel,images: List<Uri>, oldContent:ContentModel,contextResolver: ContentResolver): ContentData {
        try {
            val transactionSnapshot = database.child(id ?: "").get().await()
            var imagesUrls :List<String?>  = oldContent.images ?: emptyList()
            if(transactionSnapshot.exists()) {
                if(images.isNotEmpty()){
                    val imagesLinks = images.map { uri ->
                        val image = storage.readImageBytes(contentResolver = contextResolver, uri = uri)
//                        Log.d("IMAGES", image.toString())
                        storage.uploadImageToFirebase(image)
                    }
                    imagesUrls = imagesLinks
                }
                val transactionRef = database.child(id ?: "")
                val updatedTransaction = oldContent.copy(
                    title = content.title,
                    facilities = content.facilities,
                    description = content.description,
                    telp = content.telp,
                    price = content.price,
                    address = content.address,
                    type = content.type,
                    images = imagesUrls.filterNotNull()
                )
                transactionRef.setValue(updatedTransaction).await()
                return ContentData(data = updatedTransaction, errorMessage = null)
            }
            else {
                return ContentData(data = null, errorMessage = "transaction not found")
            }
        } catch (e : Exception) {
            return ContentData(data = null, errorMessage = e.message)
        }
    }

    override suspend fun deleteContent(id: String): Boolean {
        return try {
            val ref = database.child(id)
            ref.removeValue().await()
            true
        } catch (e: Exception) {
            throw e
        }
    }

}
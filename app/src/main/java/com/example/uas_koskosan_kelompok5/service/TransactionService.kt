package com.example.uas_koskosan_kelompok5.service

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.uas_koskosan_kelompok5.dao.TransactionDao
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.BookmarkModel
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionData
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class TransactionService(firebase: FirebaseRealtimeService, firebaseStorage: FirebaseStorageService) : TransactionDao {
    private val database = firebase.getReferenceChild("transaction")
    private val storage = firebaseStorage
    override suspend fun createtransaction(
        userid: String?,
        model: TransactionModel,
        images: List<Uri>,
        contextResolver: ContentResolver
    ): TransactionData {
        try {
            val ref = database.push()
//            Log.d("IMAGES",images.toString())
            if(images.isNotEmpty()){
                val imagesUrls = images.map { uri ->
                    val image = storage.readImageBytes(contentResolver = contextResolver, uri = uri)
//                    Log.d("IMAGES", image.toString())
                    storage.uploadImageToFirebase(image)
                }
//                Log.d("IMAGES", imagesUrls.toString())
                val data = model.copy(
                    id = ref.key,
                    cardIdentity = imagesUrls.filterNotNull()
                )
                Log.d("IMAGES", data.toString())
                ref.setValue(data).await()
                return TransactionData(data = data)
            }else {
                val data = model.copy(
                    id = ref.key,
                    cardIdentity = listOf("https://firebasestorage.googleapis.com/v0/b/uas-koskosan-kelompok5.appspot.com/o/images%2F8bacd7dd-47bd-4370-ae5d-bd0f7fee6ea7.jpg?alt=media&token=73b3103c-ac42-4052-af36-bfeae27f9e7e")
                )
                ref.setValue(data).await()
                return TransactionData(data = data)
            }
        }catch (e: Exception){
            Log.e("CONTENT", e.message.toString())
            return TransactionData(data = null, errorMessage = e.message.toString())
        }
    }
    override suspend fun getDataFromCutomer(userId: String,data: MutableState<List<TransactionModel>>) {
        val ref = database.orderByChild("customerId").equalTo(userId)

        ref?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<TransactionModel>()

                for (childSnapshot in snapshot.children) {
                    try {
                        val myData = childSnapshot.getValue(TransactionModel::class.java)
//                        val newData =
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

    override suspend fun getDataFromSeller(userId: String,data: MutableState<List<TransactionModel>>) {
        val ref = database.orderByChild("sellerId").equalTo(userId)

        ref?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<TransactionModel>()

                for (childSnapshot in snapshot.children) {
                    try {
                        val myData = childSnapshot.getValue(TransactionModel::class.java)
//                        val newData =
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
    override suspend fun getTransactionById(id: String): TransactionData {
        return try {
            val dataSnapshot = database.child(id).get().await()
            val result = dataSnapshot.getValue(TransactionModel::class.java)
            TransactionData(data = result, errorMessage = result?.let { null } ?: "Id Not Found")
        } catch (e: Exception) {
            TransactionData(data = null, errorMessage = e.message)
        }
    }

}
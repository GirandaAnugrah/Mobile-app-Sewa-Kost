package com.example.uas_koskosan_kelompok5.service

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalDate

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

    override suspend fun updateStatusTransaction(
        id: String,
        data: TransactionModel,
        newStatus: String,
    ): TransactionData {
        try {
            val transactionSnapshot = database.child(id ?: "").get().await()
            if(transactionSnapshot.exists()){
                val transactionRef = database.child(id ?: "")
                val update = data.copy(
                    status = newStatus
                )
                transactionRef.setValue(update).await()
                return TransactionData(data = data,errorMessage = null)
            }
            return TransactionData(data = null,errorMessage = "Transaction Not Found")
        }catch (e: Exception){
            return TransactionData(data = data, errorMessage = "ERROR 505 SEREVER ERROR")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateStatusAndImagePayment(
        id: String,
        status: String,
        images: List<Uri>,
        oldContent:TransactionModel,
        contextResolver: ContentResolver
    ): TransactionData {
        try {
            val transactionSnapshot = database.child(id ?: "").get().await()
            var imagesUrls :List<String?>  = oldContent.payment ?: emptyList()
            if(transactionSnapshot.exists()) {
                if(images.isNotEmpty()){
                    val imagesLinks = images.map { uri ->
                        val image = storage.readImageBytes(contentResolver = contextResolver, uri = uri)
//                        Log.d("IMAGES", image.toString())
                        storage.uploadImageToFirebase(image)
                    }
                    imagesUrls = imagesLinks
                }
                val currentDate: LocalDate = LocalDate.now()
                val newStatusPayment: MutableList<String>? = oldContent.paymentHistory?.toMutableList()
                newStatusPayment?.add(currentDate.toString())
                val transactionRef = database.child(id ?: "")
                val updatedTransaction = oldContent.copy(
                    payment = imagesUrls.filterNotNull(),
                    status = status,
                    paymentHistory = newStatusPayment?.toList()
                )
                transactionRef.setValue(updatedTransaction).await()
                return TransactionData(data = updatedTransaction, errorMessage = null)
            }
            else {
                return TransactionData(data = null, errorMessage = "transaction not found")
            }
        } catch (e : Exception) {
            return TransactionData(data = null, errorMessage = e.message)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateHistory(
        id: String,
        images: List<Uri>,
        oldContent:TransactionModel,
        contextResolver: ContentResolver
    ): TransactionData {
        try {
            val transactionSnapshot = database.child(id ?: "").get().await()
            var imagesUrls :MutableList<String>?  = oldContent.payment?.toMutableList()
            if(transactionSnapshot.exists()) {
                if(images.isNotEmpty()){
                    val imagesLinks = images.map { uri ->
                        val image = storage.readImageBytes(contentResolver = contextResolver, uri = uri)
//                        Log.d("IMAGES", image.toString())
                        storage.uploadImageToFirebase(image)
                    }
                    imagesLinks[0]?.let { imagesUrls?.add(it) }
                }
                val currentDate: LocalDate = LocalDate.now()
                val newStatusPayment: MutableList<String>? = oldContent.paymentHistory?.toMutableList()
                newStatusPayment?.add(currentDate.toString())
                val transactionRef = database.child(id ?: "")
                val updatedTransaction = oldContent.copy(
                    payment = imagesUrls,
                    startDate = currentDate.toString(),
                    paymentHistory = newStatusPayment?.toList()
                )
                transactionRef.setValue(updatedTransaction).await()
                return TransactionData(data = updatedTransaction, errorMessage = null)
            }
            else {
                return TransactionData(data = null, errorMessage = "transaction not found")
            }
        } catch (e : Exception) {
            return TransactionData(data = null, errorMessage = e.message)
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
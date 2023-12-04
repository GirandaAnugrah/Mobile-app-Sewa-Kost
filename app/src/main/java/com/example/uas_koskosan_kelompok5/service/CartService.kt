package com.example.uas_koskosan_kelompok5.service

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.uas_koskosan_kelompok5.dao.CartDao
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.BookmarkModel
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class CartService(firebase: FirebaseRealtimeService): CartDao{
    private val database = firebase.getReferenceChild("cart")
//    private val storage = firebaseStorage
    override suspend fun addToCart(userId: String?, contentData: BookmarkModel) {
        try {
            val ref = userId?.let { database.child(it).push() }
            ref!!.setValue(contentData).await()
        }catch (e: Exception){
            Log.e("CONTENT", e.message.toString())
        }
    }

    override suspend fun getData(userId: String,data: MutableState<List<ContentModel>>) {
        val ref = userId?.let { database.child(it) }

        ref?.addListenerForSingleValueEvent(object : ValueEventListener {
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

    override suspend fun deleteBookmark(userId: String,id: String) {

        val ref = database.child(userId)
        val query = ref.orderByChild("id").equalTo(id)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    // Traverse through the filtered data
                    snapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle potential errors
            }
        })
    }

}



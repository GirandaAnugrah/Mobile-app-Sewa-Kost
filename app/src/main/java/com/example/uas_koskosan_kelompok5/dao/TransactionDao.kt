package com.example.uas_koskosan_kelompok5.dao

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.MutableState
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionData
import com.example.uas_koskosan_kelompok5.model.TransactionModel

interface TransactionDao {
    suspend fun createtransaction(userid: String?, model: TransactionModel, images: List<Uri> = emptyList(), contextResolver: ContentResolver) :TransactionData
    suspend fun getDataFromCutomer(userId: String,data: MutableState<List<TransactionModel>>)
    suspend fun getDataFromSeller(userId: String,data: MutableState<List<TransactionModel>>)
    suspend fun getTransactionById(id: String): TransactionData
    suspend fun updateStatusTransaction(id: String,data: TransactionModel,newStatus: String): TransactionData

    suspend fun updateStatusAndImagePayment(
        id: String,
        status: String,
        images: List<Uri>,
        oldContent:TransactionModel,
        contextResolver: ContentResolver
    ): TransactionData

    suspend fun deleteContent(id: String): Boolean
    suspend fun updateHistory(
        id: String,
        images: List<Uri>,
        oldContent:TransactionModel,
        contextResolver: ContentResolver
    ): TransactionData
}
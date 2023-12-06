package com.example.uas_koskosan_kelompok5.state

import android.net.Uri
import com.example.uas_koskosan_kelompok5.model.ContentModel

data class TransactionState(
    val id: String? = "",
    val sellerId: String? = "",
    val customerId: String? = "",
    val name: String? = "",
    val noTelephone: String? = "",
    val gender: String? = "",
    val job: String? ="",
    val cardIdentity: List<Uri>? = emptyList(),
    val data: ContentModel? = null,
    val isCreatePostSuccessful : Boolean = false,
    val errorMessage : String?= "",
)

package com.example.uas_koskosan_kelompok5.model

import android.net.Uri
import java.time.LocalDate

data class TransactionModel(
    val id: String? = "",
    val sellerId: String? = "",
    val customerId: String? = "",
    val name: String? = "",
    val noTelephone: String? = "",
    val gender: String? = "",
    val job: String? ="",
    val status: String? = "Pending",
    val startDate: String? = null,
    val cardIdentity: List<String>? = emptyList(),
    val data: ContentModel? = null,
)

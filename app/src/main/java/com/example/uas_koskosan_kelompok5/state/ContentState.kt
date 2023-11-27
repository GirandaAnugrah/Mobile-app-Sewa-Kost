package com.example.uas_koskosan_kelompok5.state

import android.net.Uri
import com.example.uas_koskosan_kelompok5.utils.CheckBoxItems

data class ContentState(
    val id: String? = "",
    val userId: String? = "",
    val title: String? = "",
    val address: String? = "",
    val type: String? = "",
    val price: Int? = 0,
    val telp: String? = "",
    val electricity: Boolean = false,
    val bed: Boolean = false,
    val desk: Boolean = false,
    val cupboard: Boolean = false,
    val pillow: Boolean = false,
    val chair: Boolean = false,
    val facilities: List<CheckBoxItems>? = emptyList(),
    val description: String? = "",
    val images: List<Uri>? = emptyList(),
    val isCreatePostSuccessful : Boolean = false,
    val errorMessage : String?= "",
)

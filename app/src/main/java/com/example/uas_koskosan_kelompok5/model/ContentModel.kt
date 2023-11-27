package com.example.uas_koskosan_kelompok5.model

import com.example.uas_koskosan_kelompok5.utils.CheckBoxItems

data class ContentModel(
    val id: String? = "",
    val userId: String? = "",
    val title: String? = "",
    val description: String? = "",
    val address: String? = "",
    val telp: String? = "",
    val type: String? = "",
    val price: Int? = 0,
    val facilities: FacilitiesModel? = null,
    val images: List<String>? = emptyList()
)

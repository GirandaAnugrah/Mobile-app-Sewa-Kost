package com.example.uas_koskosan_kelompok5.dao

import androidx.compose.runtime.MutableState
import com.example.uas_koskosan_kelompok5.model.BookmarkModel
import com.example.uas_koskosan_kelompok5.model.ContentModel

interface CartDao {
    suspend fun addToCart(userId: String?,contentData: BookmarkModel)

    suspend fun getData(userId: String,data: MutableState<List<BookmarkModel>>)

    suspend fun deleteBookmark(userId: String,id: String)
}
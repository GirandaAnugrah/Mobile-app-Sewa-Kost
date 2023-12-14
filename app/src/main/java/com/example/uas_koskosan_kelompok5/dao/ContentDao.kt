package com.example.uas_koskosan_kelompok5.dao

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.MutableState
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.model.ContentModel
import kotlinx.coroutines.flow.Flow

interface ContentDao {
    suspend fun crateContent(userid: String?, model: ContentModel,images: List<Uri> = emptyList(),contextResolver: ContentResolver): ContentData
    fun getData(data: MutableState<List<ContentModel>>)

    suspend fun getContentById(id: String): ContentData

    suspend fun deleteContent(id: String): Boolean
    suspend fun updateContent(id: String, content: ContentModel,images: List<Uri>,oldContent: ContentModel,contextResolver: ContentResolver): ContentData
}
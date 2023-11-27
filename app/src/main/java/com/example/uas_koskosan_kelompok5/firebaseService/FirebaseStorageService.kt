package com.example.uas_koskosan_kelompok5.firebaseService

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService.ImageData
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService.getFileExtension
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

object FirebaseStorageService {
    private val storageRef: StorageReference by lazy {
        Firebase.storage.reference
    }
    data class ImageData(val data: ByteArray, val extension: String?)

    @Throws(IOException::class)
    fun readImageBytes(contentResolver: ContentResolver, uri: Uri): ImageData {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(4096)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            return ImageData(outputStream.toByteArray(), getFileExtension(contentResolver, uri))
        }

        throw IOException("Failed to read image bytes")
    }


    private fun getFileExtension(contentResolver: ContentResolver, uri: Uri): String {
        val mimeType = contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            ?: throw IllegalArgumentException("Unknown file extension")
    }


    suspend fun uploadImageToFirebase(imageData : ImageData): String? {
        try {
            Log.d("IMAGES", imageData.toString())
            val fileName = "${UUID.randomUUID()}.${imageData.extension}"
            val refStorage = storageRef.child("images/$fileName")
            val uploadTask = refStorage.putBytes(imageData.data).await()
            Log.d("IMAGES", uploadTask.toString())
            if (uploadTask.task.isSuccessful) {
                return refStorage.downloadUrl.await().toString()
            } else {
                Log.e("FirebaseStorageService", "Upload failed")
                null
            }
        } catch (e: Exception) {
            Log.e("FirebaseStorageService", "Error in uploadImageToFirebase", e)
            null
        }
        return null // Add this line to handle the case where the upload fails
    }
}
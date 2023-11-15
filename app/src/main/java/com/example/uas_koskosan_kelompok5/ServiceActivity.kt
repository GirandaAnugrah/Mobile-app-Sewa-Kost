package com.example.uas_koskosan_kelompok5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.example.uas_koskosan_kelompok5.view.service.UploadScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class ServiceActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser
    private val database = FirebaseDatabase.getInstance()
    private lateinit var images: List<Uri>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UAS_KosKosan_Kelompok5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    UploadScreen(
                        onSubmitContent = { title, facilities, description ->
                            lifecycleScope.launch {
                                store(title,facilities,description)
                            }
                        }
                    )
                }
            }
        }
    }

    private suspend fun store(title: String?, facilities: List<String>?, description: String?) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val uploadIntoDatabase = ContentModel(title, facilities, description)
            val pushData = database.reference.child("content").child(currentUser!!.uid).push()

            pushData.setValue(uploadIntoDatabase)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception("Insert data into firebase is not successful"))
                    }
                }
        }
    }

//    private suspend fun uploadMultipleFile(
//        fileUri: List<Uri>,
//        onResult: (UiState<List<Uri>>) -> Unit
//    ) {
//        try {
//            val uri: List<Uri> = withContext(Dispatchers.IO) {
//                fileUri.map { image ->
//                    async {
//                        storageReference.child(NOTE_IMAGES).child(image.lastPathSegment ?: "${System.currentTimeMillis()}")
//                            .putFile(image)
//                            .await()
//                            .storage
//                            .downloadUrl
//                            .await()
//                    }
//                }.awaitAll()
//            }
//            onResult.invoke(UiState.Success(uri))
//        } catch (e: FirebaseException){
//            onResult.invoke(UiState.Failure(e.message))
//        }catch (e: Exception){
//            onResult.invoke(UiState.Failure(e.message))
//        }
//    }




}





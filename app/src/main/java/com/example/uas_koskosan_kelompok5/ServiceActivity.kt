package com.example.uas_koskosan_kelompok5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.FacilitiesModel
import com.example.uas_koskosan_kelompok5.service.ContentService
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.example.uas_koskosan_kelompok5.view.service.UploadScreen
import com.example.uas_koskosan_kelompok5.viewmodel.ContentViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.view.component.ProgressLoadingScreen
import com.example.uas_koskosan_kelompok5.view.service.UpdateContent


class ServiceActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val contentService = ContentService(FirebaseRealtimeService, FirebaseStorageService)
        val contentId = intent.getStringExtra("ID") ?: ""


//        val isupdate = intent?.getBooleanExtra("ISUPDATE") ?:  false

        setContent {
            UAS_KosKosan_Kelompok5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val contentViewModel = viewModel<ContentViewModel>()
                    val contentState by contentViewModel.state.collectAsStateWithLifecycle()
                    var contentData by remember { mutableStateOf<ContentModel?>(null) }

                    LaunchedEffect(key1 = contentState.isCreatePostSuccessful){
                        if(contentState.isCreatePostSuccessful){
                            Toast.makeText(
                                applicationContext,
                                "Content Create Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            intoMainActivity()
                            contentViewModel.resetState()
                        }else if(contentState.errorMessage?.isNotBlank() == true){
                            Toast.makeText(
                                applicationContext,
                                "Something went wrong cannot insert data",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    LaunchedEffect(key1 = contentId){
                            val data = contentService.getContentById(contentId)
                            contentData = data.data
                    }
                    if(contentId.isBlank()){
                        UploadScreen(
                            contentState,
                            contentViewModel,
                            onSubmitContent = {
                                lifecycleScope.launch {
                                    val facilities = FacilitiesModel(
                                        electricity = contentState.electricity,
                                        bed = contentState.bed,
                                        desk = contentState.desk,
                                        cupboard = contentState.cupboard,
                                        pillow = contentState.pillow,
                                        chair = contentState.chair,
                                    )
                                    val content = ContentModel(
                                        title = contentState.title,
                                        facilities = facilities,
                                        description = contentState.description,
                                        telp = contentState.telp,
                                        price = contentState.price?.toInt(),
                                        address = contentState.address,
                                        type = contentState.type
                                    )
                                    val result = contentService.crateContent(
                                        userid = currentUser?.uid.toString(),
                                        model = content,
                                        images = contentState.images ?: emptyList(),
                                        contextResolver = contentResolver
                                    )
                                    contentViewModel.onCreateResult(result)
                                    Log.d("RESULTDATA",result.toString())
//                                    contentViewModel.resetState()
                                }
                            }
                        )
                    }else {
//                        LaunchedEffect(key1 = true){
//                            contentData?.let { contentViewModel.initData(it) }
//                        }
                        contentData?.let {
                            UpdateContent(
                                content = it,
                                state = contentState,
                                viewModel = contentViewModel,
                                onSubmitContent = {
                                    lifecycleScope.launch {
                                        val facilities = FacilitiesModel(
                                            electricity = contentState.electricity,
                                            bed = contentState.bed,
                                            desk = contentState.desk,
                                            cupboard = contentState.cupboard,
                                            pillow = contentState.pillow,
                                            chair = contentState.chair,
                                        )
                                        val content = ContentModel(
                                            title = contentState.title,
                                            facilities = facilities,
                                            description = contentState.description,
                                            telp = contentState.telp,
                                            price = contentState.price?.toInt(),
                                            address = contentState.address,
                                            type = contentState.type
                                        )
                                        val result = contentService.updateContent(
                                            id = contentData?.id ?: "",
                                            content = content,
                                            images = contentState.images ?: emptyList(),
                                            oldContent = contentData!!,
                                            contextResolver = contentResolver
                                        )
                                        contentViewModel.onCreateResult(result)
//                                        contentViewModel.resetState()
                                    }
                                })
                        }
//                        Text(text = contentData.toString())
                    }
                }
            }
        }
    }
    private fun intoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    @Composable
    fun launchLoading(param: Boolean) {
        ProgressLoadingScreen(isLoading = param)
    }

}





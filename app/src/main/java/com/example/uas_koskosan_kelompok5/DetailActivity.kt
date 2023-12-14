package com.example.uas_koskosan_kelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.BookmarkModel
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.service.CartService
import com.example.uas_koskosan_kelompok5.service.ContentService
import com.example.uas_koskosan_kelompok5.service.TransactionService
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.example.uas_koskosan_kelompok5.view.content.DetailsScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.util.UUID

class DetailActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentService = ContentService(FirebaseRealtimeService, FirebaseStorageService)
        val cartService = CartService(FirebaseRealtimeService)
        val contentId = intent.getStringExtra("ID") ?: ""
        setContent{
            var contentData by remember { mutableStateOf<ContentModel?>(null) }
            LaunchedEffect(key1 = contentId){
                val data = contentService.getContentById(contentId)
                contentData = data.data
            }
            UAS_KosKosan_Kelompok5Theme {
                contentData?.let {
                    DetailsScreen(
                        item = it,
                        firebaseUser = currentUser,
                        deleteContent = {id ->
                            lifecycleScope.launch {
                                try {
                                    contentService.deleteContent(id)
                                    Toast.makeText(
                                        applicationContext,
                                        "Delete Data Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    intoMainActivity()
                                }catch (e: Exception){
                                    Toast.makeText(
                                        applicationContext,
                                        "Cannot Delete Data",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        updateContent = {id ->
                            lifecycleScope.launch {
                                intoServiceActivityUpdate(id)
                            }
                        },
                        addToChart = {id ->
                            lifecycleScope.launch{
                                try {
                                    val data = contentService.getContentById(id)
                                    var bookmark = BookmarkModel(
                                        id = UUID.randomUUID().toString(),
                                        data = data.data
                                    )
//                                    bookmark.copy(id = bookmark.)
                                    cartService.addToCart(currentUser?.uid,
                                        bookmark
                                    )
                                    Toast.makeText(
                                        applicationContext,
                                        "Add to chart successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }catch (e: Exception){
                                    Toast.makeText(
                                        applicationContext,
                                        "Cannot Add To Chart",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        buyContent = {id ->
                            lifecycleScope.launch {
                                intoTransactionActivity(id)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun intoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun intoServiceActivityUpdate(id: String) {
        val intent = Intent(this, ServiceActivity::class.java)
        intent.putExtra("ID",id)
        startActivity(intent)
    }
    private fun intoTransactionActivity(id: String) {
        val intent = Intent(this, TransactionActivity::class.java)
        intent.putExtra("ID",id)
        startActivity(intent)
    }
}
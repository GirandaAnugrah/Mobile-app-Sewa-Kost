package com.example.uas_koskosan_kelompok5

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.navigation.Screen
import com.example.uas_koskosan_kelompok5.service.ContentService
import com.example.uas_koskosan_kelompok5.service.TransactionService
import com.example.uas_koskosan_kelompok5.state.TransactionState
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.example.uas_koskosan_kelompok5.view.component.CalendarScreen
import com.example.uas_koskosan_kelompok5.view.transaction.PaymentScreen
import com.example.uas_koskosan_kelompok5.view.transaction.RequestTransaction
import com.example.uas_koskosan_kelompok5.viewmodel.ContentViewModel
import com.example.uas_koskosan_kelompok5.viewmodel.TransactionViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class TransactionActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentService = ContentService(FirebaseRealtimeService, FirebaseStorageService)
        val contentId = intent.getStringExtra("ID") ?: ""
        val transactionId = intent.getStringExtra("TRANSACTION") ?: ""
        val transactionService = TransactionService(FirebaseRealtimeService, FirebaseStorageService)
        setContent {
            val transactionViewModel = viewModel<TransactionViewModel>()
            val transactionState by transactionViewModel.state.collectAsStateWithLifecycle()
            var contentData by remember { mutableStateOf<ContentModel?>(null) }
            var transactionData by remember { mutableStateOf<TransactionModel?>(null) }
            LaunchedEffect(key1 = contentId){
                val data = contentService.getContentById(contentId)
                contentData = data.data
            }
            LaunchedEffect(key1 = transactionId){
                val data = transactionService.getTransactionById(transactionId)
                transactionData = data.data
            }
            LaunchedEffect(key1 = transactionState.isCreatePostSuccessful){
                if(transactionState.isCreatePostSuccessful){
                    Toast.makeText(
                        applicationContext,
                        "Content Create Successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    intoMainActivity()
                    transactionViewModel.resetState()
                }else if(transactionState.errorMessage?.isNotBlank() == true){
                    Toast.makeText(
                        applicationContext,
                        "Something went wrong cannot insert data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            UAS_KosKosan_Kelompok5Theme {
                contentData?.let { transactionData?.let { it1 ->
                    TransactionNavigation(it,
                        it1,transactionViewModel, transactionState,transactionService)
                } }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun TransactionNavigation(item: ContentModel,transactionData: TransactionModel, transactionViewModel: TransactionViewModel, transactionState: TransactionState,transactionService: TransactionService) {
        val navController = rememberNavController()
        var transactionDay by remember { mutableStateOf<LocalDate?>(null) }
//        var dest = "calendar"
        var dest by remember { mutableStateOf("calendar") }
        if (!transactionData.id.isNullOrBlank()){
            dest = "payment"
        }
        NavHost(navController,startDestination = dest){
            composable("calendar"){
                CalendarScreen(addDay = {day ->
                    lifecycleScope.launch {
                        transactionDay = day
                        navController.navigate("transaction")
                    }
                })
            }
            composable("transaction"){
                currentUser?.let { it1 ->
                    RequestTransaction(
                        currentUser = it1,
                        transactionState = transactionState,
                        transactionViewModel = transactionViewModel,
                        content = item,
                        addToRent = {
                            lifecycleScope.launch {
                                val rent = TransactionModel(
                                    sellerId = item.userId,
                                    customerId = currentUser!!.uid,
                                    name = transactionState.name,
                                    noTelephone = transactionState.noTelephone,
                                    gender = transactionState.gender,
                                    job = transactionState.job,
                                    startDate = transactionDay.toString(),
                                    data = item
                                )
                                val result = transactionService.createtransaction(
                                    userid = currentUser?.uid.toString(),
                                    model = rent,
                                    images = transactionState.cardIdentity ?: emptyList(),
                                    contextResolver = contentResolver
                                    )
                                transactionViewModel.onCreateResult(result)
                            }
                        }
                    )
                }
            }
            composable("payment"){
                PaymentScreen(
                    transaction = transactionData,
                    viewModel = transactionViewModel,
                    state = transactionState,
                    payment = {
                        lifecycleScope.launch {
                            intoMainActivity()
                        }
                    }
                )
            }
        }
    }
    private fun intoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
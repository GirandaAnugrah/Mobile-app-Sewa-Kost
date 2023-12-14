package com.example.uas_koskosan_kelompok5

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.service.TransactionService
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.example.uas_koskosan_kelompok5.view.myKost.DetailKostSeller
import com.example.uas_koskosan_kelompok5.view.transaction.DetailTransactionCustomer
import com.example.uas_koskosan_kelompok5.view.transaction.DetailTransactionSeller
import com.example.uas_koskosan_kelompok5.view.transaction.PaymentScreen
import com.example.uas_koskosan_kelompok5.view.transaction.SellerAcceptScreen
import com.example.uas_koskosan_kelompok5.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch

class SellerTransactionActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transactionId = intent.getStringExtra("TRANSACTION") ?: ""
        val transactionService = TransactionService(FirebaseRealtimeService, FirebaseStorageService)
        setContent {
            val transactionViewModel = viewModel<TransactionViewModel>()
            val transactionState by transactionViewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = transactionState.isCreatePostSuccessful){
                if(transactionState.isCreatePostSuccessful){
                    Toast.makeText(
                        applicationContext,
                        "Payment Successfully",
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
            UAS_KosKosan_Kelompok5Theme{
//                Text(text = "THIS SIS OADJAJKJDKj")
                Column {
//                    Text("THIS IS SELLER TRANSACTION ACTIVITY")
                    var transactionData by remember { mutableStateOf<TransactionModel?>(null) }
                    LaunchedEffect(key1 = transactionId){
                        val data = transactionService.getTransactionById(transactionId)
                        transactionData = data.data
                    }
                    if (transactionData?.status == getString(R.string.pending)){
//                        TEST()
                        transactionData?.let { SellerAcceptScreen(
                            data = it,
                            updateStatus = {transaction ->
                                lifecycleScope.launch {
                                    transaction.id?.let { it1 ->
                                        transactionService.updateStatusTransaction(
                                            it1,transaction,getString(R.string.payment))
                                    }
                                    intoMainActivity()
                                }
                            }
                        )
                        }
                    }else if(transactionData?.status == getString(R.string.diperiksa)){
                        DetailTransactionSeller(data = transactionData!!,
                            updateStatus = {transaction ->
                                lifecycleScope.launch {
                                    transaction.id?.let { it1 ->
                                        transactionService.updateStatusTransaction(
                                            it1,transaction,getString(R.string.success))
                                    }
                                    intoMainActivity()
                                }
                            }
                        )
                    }else if(transactionData?.status == getString(R.string.success)){
                        DetailKostSeller(
                            data = transactionData!!,
                            delete = {id ->
                                lifecycleScope.launch {
                                    try {
                                        transactionService.deleteContent(id)
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
                            }
                        )
                    }else if(transactionData?.status == getString(R.string.payment)) {
                        DetailTransactionCustomer(transactionData!!)
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
}
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
import com.example.uas_koskosan_kelompok5.view.myKost.DetailMyKost
import com.example.uas_koskosan_kelompok5.view.transaction.DetailTransactionCustomer
import com.example.uas_koskosan_kelompok5.view.transaction.PaymentScreen
import com.example.uas_koskosan_kelompok5.viewmodel.TransactionViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class CustomerTransactionActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transactionId = intent.getStringExtra("TRANSACTION") ?: ""
        val paymentID = intent.getStringExtra("PAYMENT") ?: ""
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
                Column {
                    var transactionData by remember { mutableStateOf<TransactionModel?>(null) }
                    LaunchedEffect(key1 = transactionId){
                        val data = transactionService.getTransactionById(transactionId)
                        transactionData = data.data
                    }
                    if(!paymentID.isNullOrBlank()){
                        transactionData?.let {
                            PaymentScreen(
                                transaction = it,
                                viewModel = transactionViewModel,
                                state = transactionState,
                                payment = {
                                    lifecycleScope.launch {
                                        val result = transactionData!!.id?.let { it1 ->
                                            transactionService.updateHistory(
                                                it1,
                                                transactionState.cardIdentity ?: emptyList(),
                                                transactionData!!,
                                                contentResolver
                                            )
                                        }
                                        if (result != null) {
                                            transactionViewModel.onCreateResult(result)
                                        }
                                        intoMainActivity()
                                    }
                                }
                            )
                        }
                    }else if (transactionData?.status == getString(R.string.payment)){
                        transactionData?.let {
                            PaymentScreen(
                                transaction = it,
                                viewModel = transactionViewModel,
                                state = transactionState,
                                payment = {
                                    lifecycleScope.launch {
                                        val result = transactionData!!.id?.let { it1 ->
                                            transactionService.updateStatusAndImagePayment(
                                                it1,
                                                getString(R.string.diperiksa),
                                                transactionState.cardIdentity ?: emptyList(),
                                                transactionData!!,
                                                contentResolver
                                            )
                                        }
                                        if (result != null) {
                                            transactionViewModel.onCreateResult(result)
                                        }
                                        intoMainActivity()
                                    }
                                }
                            )
                        }
                    }else if(transactionData?.status == getString(R.string.pending) || transactionData?.status == getString(R.string.diperiksa)){
                        DetailTransactionCustomer(transactionData!!)
                    }else if(transactionData?.status == getString(R.string.success)){
                        DetailMyKost(
                            transactionData!!,
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
                            },
                            update = {id ->
                                refreshActivity(id)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun refreshActivity(id: String,) {
        val intent = getIntent()
        intent.putExtra("ID",id)
        intent.putExtra("PAYMENT",id)
        startActivity(intent)
    }

    private fun intoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
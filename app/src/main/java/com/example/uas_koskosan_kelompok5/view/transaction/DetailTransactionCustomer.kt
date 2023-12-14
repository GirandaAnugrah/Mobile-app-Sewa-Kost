package com.example.uas_koskosan_kelompok5.view.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.view.component.ProgressLoadingScreen

@Composable
fun DetailTransactionCustomer(
    data: TransactionModel
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
            .padding(horizontal = 8.dp)
    ) {
        Text(text = "Agreement", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))
        data.data?.let { SellerRequestCard(it) }
        Text(text = "Name : ${data.name}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Gender : ${data.gender}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Job : ${data.job}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Start Date : ${data.startDate}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "No Telp : ${data.noTelephone}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Status : ${data.status}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "KTP")
        AsyncImage(
            model = data.cardIdentity?.get(0) ?: "https://firebasestorage.googleapis.com/v0/b/uas-koskosan-kelompok5.appspot.com/o/images%2F8bacd7dd-47bd-4370-ae5d-bd0f7fee6ea7.jpg?alt=media&token=73b3103c-ac42-4052-af36-bfeae27f9e7e",
            contentDescription = null ,
            modifier = Modifier
                .height(170.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(15.dp))
        if(!data.payment.isNullOrEmpty()){
            Text(text = "Bukti Pembayaran")
            AsyncImage(
                model = data.payment?.get(0),
                contentDescription = null ,
                modifier = Modifier
                    .height(170.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
//        ProgressLoadingScreen(isLoading = isLoading)
    }
}
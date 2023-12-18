package com.example.uas_koskosan_kelompok5.view.transaction

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.view.component.ProgressLoadingScreen

@Composable
fun DetailTransactionCustomer(
    data: TransactionModel
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val onBackPressedDispatcher = (context as? OnBackPressedDispatcherOwner)?.onBackPressedDispatcher

    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
            .padding(horizontal = 8.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Back",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onBackPressedDispatcher?.onBackPressed()
                    }
                )
            }
        }

        Text(
            text = "Agreement",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
        data.data?.let { SellerRequestCard(it) }
        if(!data.payment.isNullOrEmpty()){
            Text(text = "Bukti Pembayaran")
            Spacer(modifier = Modifier.height(20.dp))
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

    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
            .padding(horizontal = 8.dp)
            .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Name")
                Text(
                    text = "${data.name}",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                        .wrapContentWidth(Alignment.End)
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Gender")
                Text(
                    text = "${data.gender}",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                        .wrapContentWidth(Alignment.End)
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Job")
                Text(
                    text = "${data.job}",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                        .wrapContentWidth(Alignment.End)
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Start date")
                Text(
                    text = "${data.startDate}",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                        .wrapContentWidth(Alignment.End)
                )
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "No. telp")
                Text(
                    text = "${data.noTelephone}",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                        .wrapContentWidth(Alignment.End)
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Status")
                Text(
                    text = "${data.status}",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp)
                        .wrapContentWidth(Alignment.End)
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
            ){
                Text(text = "KTP")
                AsyncImage(
                    model = data.cardIdentity?.get(0) ?: "https://firebasestorage.googleapis.com/v0/b/uas-koskosan-kelompok5.appspot.com/o/images%2F8bacd7dd-47bd-4370-ae5d-bd0f7fee6ea7.jpg?alt=media&token=73b3103c-ac42-4052-af36-bfeae27f9e7e",
                    contentDescription = null ,
                    modifier = Modifier
                        .height(170.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

        }
    }
}
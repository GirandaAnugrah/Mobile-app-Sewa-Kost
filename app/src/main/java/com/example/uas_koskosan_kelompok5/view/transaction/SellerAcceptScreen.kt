package com.example.uas_koskosan_kelompok5.view.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.view.component.ProgressLoadingScreen

@Composable
fun SellerAcceptScreen(
    data: TransactionModel,
    updateStatus: (transaction: TransactionModel) -> Unit
) {
    val scrollState = rememberScrollState()
    var isLoading by remember { mutableStateOf(false) }
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
        AsyncImage(
            model = data.cardIdentity?.get(0),
            contentDescription = null ,
            modifier = Modifier
                .height(170.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        ProgressLoadingScreen(isLoading = isLoading)

        Button(onClick = {
            updateStatus(data)
            isLoading = true
        },
            modifier = Modifier.fillMaxSize(),
            enabled = data.status != "Menunggu Pembayaran"
        ) {
            Text(text = "Accept")
        }

    }
}



@Composable
fun SellerRequestCard(item: ContentModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Row {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                AsyncImage(
                    model = item.images?.get(0),
                    contentDescription = null ,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp),
                    contentScale = ContentScale.Crop
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Text(
                    text = item.title ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rp. ${item.price.toString()}" ,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.type.toString(),
                    fontSize = 15.sp,
                    color = Color.Gray
                )

            }
        }
    }
}
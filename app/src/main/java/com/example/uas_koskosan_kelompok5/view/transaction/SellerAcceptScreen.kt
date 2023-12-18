package com.example.uas_koskosan_kelompok5.view.transaction

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

    }

    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
            .padding(horizontal = 8.dp)
            .padding(16.dp),
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
                Text(text = "Foto KTP")
            }
        }
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
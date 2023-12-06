package com.example.uas_koskosan_kelompok5.view.transaction

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.state.TransactionState
import com.example.uas_koskosan_kelompok5.viewmodel.TransactionViewModel

@Composable
fun PaymentScreen(
    transaction: TransactionModel,
    viewModel: TransactionViewModel,
    state: TransactionState,
    payment: () -> Unit
) {
    val scrollState = rememberScrollState()
//    Text(text = transaction.toString())
    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
    ) {
        transaction.data?.let { PaymentCard(item = it) }
//        Text(text = transaction.toString())
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = transaction.data?.price.toString())
        Spacer(modifier = Modifier.height(50.dp))
        PaymentProof(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = {
            payment()
        }) {
            Text(text = "Send")
        }
    }
}


@Composable
fun PaymentProof(viewModel: TransactionViewModel, state: TransactionState) {
    Text(text = "Masukkan Bukti Pembayaran")
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            viewModel.onImageChange(it)
        }
    )
    LazyRow{
        items(state.cardIdentity ?: emptyList()){ uri ->
            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(248.dp))
        }
    }
    Button(onClick = {
        multiplePhotoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    },
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        Text("Upload Bukti")
    }
}

@Composable
fun PaymentCard(item: ContentModel) {
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

@Composable
fun CutomerInformation(item: TransactionModel) {
    Column {

    }
}
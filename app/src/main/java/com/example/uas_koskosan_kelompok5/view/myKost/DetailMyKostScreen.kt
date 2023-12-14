package com.example.uas_koskosan_kelompok5.view.myKost

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.uas_koskosan_kelompok5.view.transaction.SellerRequestCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailMyKost(
    data: TransactionModel,
    delete: (id: String) -> Unit,
    update: (id: String) -> Unit
) {
    val scrollState = rememberScrollState()
    var question by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val date = LocalDate.parse(data.startDate, formatter)
    val formattedDate = date.format(DateTimeFormatter.ofPattern("dd, MMMM yyyy", Locale.ENGLISH))
    val dateOneMonthLater = date.plusMonths(1)
    val endDate = dateOneMonthLater.format(DateTimeFormatter.ofPattern("dd, MMMM yyyy", Locale.ENGLISH))
    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
            .padding(horizontal = 8.dp)
    ) {
        Text(text = "MY KOST", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))
        data.data?.let { SellerRequestCard(it) }
        Text(text = "Name : ${data.name}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Gender : ${data.gender}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Job : ${data.job}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Start Date : $formattedDate")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "End Date : $endDate")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "No Telp : ${data.noTelephone}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Status : ${data.status}")
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Payment History")
        Spacer(modifier = Modifier.height(15.dp))
        for (dt in data.paymentHistory!!) {
            Text(text = " - $dt BCA Nominal Rp.${data.data?.price.toString()}")
            Spacer(modifier = Modifier.height(15.dp))
        }
//        if(!data.payment.isNullOrEmpty()){
//            Text(text = "Bukti Pembayaran")
//            AsyncImage(
//                model = data.payment?.get(0),
//                contentDescription = null ,
//                modifier = Modifier
//                    .height(170.dp)
//                    .fillMaxWidth(),
//                contentScale = ContentScale.Crop
//            )
//        }
        if (question) {
            AlertDialog(
                icon = {
                    Icon(Icons.Default.Delete, contentDescription = "Example Icon")
                },
                title = {
                    Text(text = "Warning")
                },
                text = {
                    Text(text = "Are you sure want to delete this content")
                },
                onDismissRequest = {
                    question = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            delete(data.id ?: "")
                            isLoading = true
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            question = false
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
        ProgressLoadingScreen(isLoading = isLoading)
        Button(onClick = {
            question = true
        }, modifier = Modifier.fillMaxWidth()){
            Text(text = "Tinggalkan Kost")
        }
        Button(onClick = {
            update(data.id ?:"")
        }, modifier = Modifier.fillMaxWidth()){
            Text(text = "Lanjut Kost")
        }
    }
}
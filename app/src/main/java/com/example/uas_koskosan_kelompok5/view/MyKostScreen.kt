package com.example.uas_koskosan_kelompok5.view

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.model.BookmarkModel
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import java.time.temporal.ChronoUnit

@Composable
fun MyKostScreen(
    items: List<TransactionModel>,
    intoDetailTransaction: (id: String) -> Unit
) {
    val sortedItems = items.sortedByDescending { it.data?.id ?: "" }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "My Kost", fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(Color.White),
            ) {
                itemsIndexed(sortedItems){index, item ->
                    item.data?.let {
                        if(item.status == stringResource(id = R.string.success)){
                            MyKostCard(
                                transaction = item,
                                item = it,
                                intoDetailTransaction
                            )
                        }
                    }
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyKostCard(
    transaction: TransactionModel,
    item: ContentModel,
    intoDetailTransaction: (id: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White)
            .clickable { intoDetailTransaction(transaction.id ?: "") },
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
                    text = "${transaction.name.toString()}" ,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
//                val nextMonth = transaction.startDate.plusMonth(1)
                Text(
                    text = "${transaction.startDate.toString()}" ,
                    fontSize = 15.sp,
                    color = Color.Gray
                )

            }
        }
    }
}
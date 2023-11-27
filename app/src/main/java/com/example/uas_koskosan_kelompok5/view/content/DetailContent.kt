package com.example.uas_koskosan_kelompok5.view.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel

@Composable
fun DetailsScreen(item: ContentModel,
                  deleteContent:(id: String) -> Unit,
                  updateContent:(id: String) -> Unit) {

    var question by remember { mutableStateOf(false) }

    Column {
        LazyRow{
            itemsIndexed(item.images ?: emptyList()){index, uri ->
                AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(248.dp))
            }
        }
        Text(text = item.title.toString())
        Text(text = item.address.toString())
        Text(text = item.price.toString())
        Text(text = item.description.toString())
        Text(text = item.facilities?.bed.toString())

        if (question) {
            Dialog(
                onDismissRequest = { /* Nothing */ },
                properties = DialogProperties(
                    dismissOnClickOutside = false)

            ) {
                Box(
                    modifier = Modifier
                        .width(3000.dp)
                        .height(200.dp)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clip(RoundedCornerShape(16.dp))
                ) {
//                CircularProgressIndicator(color = Color.White)
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Are You Sure want to delete this content")
                    }
//                    Text(text = "Are You Sure want to delete this content")
                    Row (
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ){
                        Button(onClick = {
                            question = false
                        }) {
                            Text(text = "No")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            deleteContent(item.id ?: "")
                        }) {
                            Text(text = "Yes")
                        }
                    }

                }
            }
        }

        Button(onClick = {
            question = true
        }) {
            Text(text = "Delete This Content")
        }

        Button(onClick = {
            updateContent(item.id ?: "")
        }) {
            Text(text = "Update this content")
        }
    }
}

@Composable
fun Hero(isLoading: Boolean) {
    if (isLoading) {
        Dialog(
            onDismissRequest = { /* Nothing */ },
            properties = DialogProperties(
                dismissOnClickOutside = false)

        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
//                CircularProgressIndicator(color = Color.White)
                Text(text = "Are You Sure want to delete this content")
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "No")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Yes")
                }
            }
        }
    }
}
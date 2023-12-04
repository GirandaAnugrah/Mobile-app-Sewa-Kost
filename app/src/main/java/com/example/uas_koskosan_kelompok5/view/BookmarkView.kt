package com.example.uas_koskosan_kelompok5.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.model.ContentModel

@Composable
fun BookmarkView(
    items: List<ContentModel>,
    deleteBookmark: (id: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Bookmarks", fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(Color.White)
            ) {
                itemsIndexed(items){index, item ->
                    BookmarkCard(item = item, deleteBookmark)
//                    Text(text = item.title.toString())
                }
               
            }
        }
    }
}

@Composable
fun BookmarkCard(item: ContentModel, deleteBookmark: (id: String) -> Unit) {
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
                Row {
                    Text(
                        text = item.type.toString(),
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(onClick = {
                        deleteBookmark(item.id ?: "")
                    }) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .height(30.dp)
//                                ) {
//                                    IconWithText(icon = painterResource(id = R.drawable.icon_bed), name = "3 Bedroom")
//                                    IconWithText(icon = painterResource(id = R.drawable.icon_bed), name = "1 Bathroom")
//                                }
            }
        }
    }
}
@Composable
fun IconWithText(icon:Painter,name:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(7.dp)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = "Bed icon",
            tint = Color.Gray // Ganti dengan warna ikon yang Anda inginkan
        )
        Text(
            text = name,
            fontSize = 16.sp,
            color = Color.Black, // Ganti dengan warna teks yang Anda inginkan
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


//
//@Preview(showBackground = true)
//@Composable
//fun CardExamplePreview() {
//    BookmarkView()
//}
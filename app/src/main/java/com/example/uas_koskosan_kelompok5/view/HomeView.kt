package com.example.uas_koskosan_kelompok5.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.navigation.Screen
import com.example.uas_koskosan_kelompok5.view.component.ProductCard
import com.google.firebase.auth.FirebaseUser


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    currentUser: FirebaseUser?,
    contentModel: List<ContentModel>,
    navigateToDetails: (carId: String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        var search = ""
        Row() {
            TextField(
                value = search,
                onValueChange = {
                    search = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) // Adjust alpha as needed
                    .clip(MaterialTheme.shapes.small))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2) // Jumlah kolom dalam grid (misalnya 3)
        ) {
            itemsIndexed(contentModel) {index, item ->

                item.price?.let { item?.images?.let { it1 -> item.title?.let { it2 -> item.type?.let { it3 -> item.id?.let { it4 -> ProductCard(
                    imageUrl = it1.get(0),
                    title = it2,
                    price = it,
                    type = it3, id = it4){
                    navigateToDetails(it4 ?: "")
                }
                } } } } }
            }
        }
    }
}


@Composable
fun GridItem(item: ContentModel) {
    Text(text = item.toString())
}
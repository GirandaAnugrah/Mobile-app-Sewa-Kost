package com.example.uas_koskosan_kelompok5.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.view.component.ProductCard


@Composable
fun HomeView(navController: NavController, contentModel: List<ContentModel>, navigateToDetails: (carId: String) -> Unit) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Jumlah kolom dalam grid (misalnya 3)
            modifier = Modifier.padding(12.dp)
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
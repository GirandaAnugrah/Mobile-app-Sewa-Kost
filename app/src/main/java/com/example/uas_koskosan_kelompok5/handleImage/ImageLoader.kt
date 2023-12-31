package com.example.uas_koskosan_kelompok5.handleImage


import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
@Composable
fun LoadImage(url: String, contentDescription: String? = null) {
    Image(
        painter = rememberAsyncImagePainter(model = url),
        contentDescription = contentDescription,
    )
}

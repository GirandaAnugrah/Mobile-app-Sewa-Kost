package com.example.uas_koskosan_kelompok5.handleImage


import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import coil.compose.rememberImagePainter
@Composable
fun LoadImage(url: String, contentDescription: String? = null) {
    Image(
        painter = rememberImagePainter(data = url),
        contentDescription = contentDescription,
    )
}

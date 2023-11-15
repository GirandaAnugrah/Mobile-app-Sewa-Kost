package com.example.uas_koskosan_kelompok5.view.service

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun UploadImages() {
    var images by remember { mutableStateOf(mutableListOf<Uri>()) }
    Column {

    }
}
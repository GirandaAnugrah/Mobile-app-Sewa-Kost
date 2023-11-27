package com.example.uas_koskosan_kelompok5.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.handleImage.LoadImage

@Composable
fun ProductCard(
    imageUrl: String, // Gambar diambil dari resource
    title: String,
    price: Int,
    type: String,
    id: String,
    navigateToDetails : (id : String?) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(250.dp)
            .clickable{ navigateToDetails(id)},
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
//            imageUrl?.let { LoadImage(url = it,null) }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = type,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp. $price",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}


//@Preview
//@Composable
//fun ProductCardPreview() {
//    ProductCard(
//        imageUrl = R.drawable.sample_image, // Gambar contoh dari resource drawable
//        title = "Product Title",
//        price = "$99.99"
//    )
//}

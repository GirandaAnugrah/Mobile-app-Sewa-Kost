package com.example.uas_koskosan_kelompok5.view.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.FacilitiesModel
import com.google.firebase.auth.FirebaseUser


@Composable
fun DetailsScreen(
    item: ContentModel,
    firebaseUser: FirebaseUser?,
    deleteContent:(id: String) -> Unit,
    updateContent:(id: String) -> Unit,
    addToChart: (id: String) -> Unit
) {
    var question by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize().verticalScroll(enabled = true, state = scrollState)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            ){
                itemsIndexed(item.images ?: emptyList()){index, uri ->
                    item.images?.let { DetailImage(index, uri, it.size) }
                }
            }
            ContentInformation(name = item.title ?: "null", address = item.address ?: "null", price = item.price.toString())
            item.facilities?.let { ListFacility(facilities = it) }
            Spacer(modifier = Modifier.height(16.dp))
            item.description?.let { DetailDescription(desc = it) }
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
                                deleteContent(item.id ?: "")
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
            if(firebaseUser?.uid == item.userId){
                Row {
                    Button(
                        onClick = {
                            question = true
                        },
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = {
                            updateContent(item.id ?: "")
                        },
                    ) {
                        Text(
                            text = "Update",
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    addToChart(item.id ?: "")
                },
            ) {
                Text(
                    text = "Add To Chart",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun DetailImage(index: Int, uri: String, total: Int) {
    AsyncImage(model = uri,
        contentDescription = null,
        modifier = Modifier
            .size(248.dp)
            .padding(
                start = if (index == 0) 16.dp else 8.dp,
                end = if (index == total - 1) 16.dp else 0.dp
            ),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ContentInformation(name: String, address: String, price: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = address,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = "Harga: $price",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ListFacility(facilities: FacilitiesModel) {
    Text(
        text = "Fasilitas",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (facilities.electricity == true){
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Kasur",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Listrik", modifier = Modifier.padding(15.dp,0.dp))
            }

        }
        if(facilities?.bed == true){
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Kasur",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Kasur", modifier = Modifier.padding(15.dp,0.dp))
            }
        }
        if (facilities?.desk == true){
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Kasur",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Meja", modifier = Modifier.padding(15.dp,0.dp))
            }
        }
        if (facilities?.chair == true){
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Kasur",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Kursi", modifier = Modifier.padding(15.dp,0.dp))
            }
        }
        if (facilities?.cupboard == true){
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Kasur",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Lemari", modifier = Modifier.padding(15.dp,0.dp))
            }
        }
        if (facilities?.pillow == true){
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Kasur",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Bantal", modifier = Modifier.padding(15.dp,0.dp))
            }
        }

    }
}


@Composable
fun DetailDescription(desc: String) {
    // Deskripsi
    Text(
        text = "Deskripsi",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface
    )
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
package com.example.uas_koskosan_kelompok5.view.content

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.FacilitiesModel
import com.google.firebase.auth.FirebaseUser


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    item: ContentModel,
    firebaseUser: FirebaseUser?,
    deleteContent:(id: String) -> Unit,
    updateContent:(id: String) -> Unit,
    addToChart: (id: String) -> Unit,
    buyContent: (id: String) -> Unit
) {
    var question by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = {
            NavigationBar {
                Surface {
                    
                }
                Column(
                    modifier = Modifier
                        .width(130.dp)
                        .align(alignment = Alignment.CenterVertically)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "Rp. ${item.price.toString()}",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                NavigationBarItem(selected = true,
                    onClick = {
                        buyContent(item.id ?: "")
                    },
                    icon = {
                        Box {
                            Text(text = "Buy Now")
                        }
                    })
            }
//            Row(
//                modifier = Modifier
//                    .background(Color.White)
//                    .padding(5.dp, 10.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text(
//                        text = "Rp. ${item.price.toString()}",
//                        fontSize = 18.sp,
//                    )
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                Box(
//                    modifier = Modifier
//                        .background(Color.Blue)
//                        .clickable {
//                            addToChart(item.id ?: "")
//                        }
//                ) {
//                    Text(
//                        text = "Add To Chart",
//                        fontSize = 18.sp,
//                    )
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                Box(
//                    modifier = Modifier
//                        .background(Color.Blue)
//                        .clickable {
//                            buyContent(item.id ?: "")
//                        }
//                ) {
//                    Text(
//                        text = "Buy Now",
//                        fontSize = 18.sp,
//                    )
//                }
//            }
        }
    ) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = true, state = scrollState)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Row(

                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Back",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        //
                    }
                )
            }

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

//            Row {
//                Button(
//                    onClick = {
//                        addToChart(item.id ?: "")
//                    },
//                ) {
//                    Text(
//                        text = "Add To Chart",
//                        color = Color.White,
//                        style = TextStyle(fontWeight = FontWeight.Bold)
//                    )
//                }
//                Spacer(modifier = Modifier.width(20.dp))
//                Button(
//                    onClick = {
//                        buyContent(item.id ?: "")
//                    },
//                ) {
//                    Text(
//                        text = "Buy Now",
//                        color = Color.White,
//                        style = TextStyle(fontWeight = FontWeight.Bold)
//                    )
//                }
//            }
            if(firebaseUser?.uid == item.userId){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {
                            question = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            updateContent(item.id ?: "")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Update",
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
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
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier,
        fontSize = 26.sp
    )
    Text(
        text = address,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp
    )
    Text(
        text = "Rp $price",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ListFacility(facilities: FacilitiesModel) {
    Text(
        text = "Fasilitas",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp
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
                    imageVector = Icons.Default.Star,
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
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(bottom = 6.dp)
    )
    Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(bottom = 24.dp)
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
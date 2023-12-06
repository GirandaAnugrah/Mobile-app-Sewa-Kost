package com.example.uas_koskosan_kelompok5.view.transaction

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.state.TransactionState
import com.example.uas_koskosan_kelompok5.view.component.ProgressLoadingScreen
import com.example.uas_koskosan_kelompok5.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun RequestTransaction(
    currentUser: FirebaseUser,
    transactionState: TransactionState,
    transactionViewModel: TransactionViewModel,
    content: ContentModel,
    addToRent: () -> Unit
) {
    val scrollState = rememberScrollState()
    val priceAdmin = (content.price?.times(5) ?: 0) / 100
    val priceTotal = content.price?.plus(priceAdmin)
    var isLoading by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
    ) {

        Text(text = "Request Rent")
        RequestCard(item = content)
        
        Text(text = "Personal Data")
        InputPersonalData(viewModel = transactionViewModel, state = transactionState)
        ImageDataCard(viewModel = transactionViewModel, state = transactionState)
        Spacer(modifier = Modifier.height(30.dp))
        if (priceTotal != null) {
            CalculatePrice(content = content,priceAdmin,priceTotal)
        }
        ProgressLoadingScreen(isLoading = isLoading)
        Row {
            Text(text = priceTotal.toString())
            Spacer(modifier = Modifier.width(40.dp))
            Button(onClick = {
                isLoading =true
                addToRent()
            }) {
                Text(text = "Ajukan Sewa")
            }
        }
    }
}

@Composable
fun CalculatePrice(content: ContentModel, priceAdmin: Int, priceTotal: Int) {
    Row {
        Text(text = "Price")
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = content.price.toString())
    }
    Row {
        Text(text = "Biaya Admin")
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = priceAdmin.toString())
    }
    Row {
        Text(text = "Total")
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = priceTotal.toString())
    }
}

@Composable
fun ImageDataCard(viewModel: TransactionViewModel,state: TransactionState) {
    Text(text = "Foto KTP")
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            viewModel.onImageChange(it)
        }
    )
    LazyRow{
        items(state.cardIdentity ?: emptyList()){ uri ->
            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(248.dp))
        }
    }
    Button(onClick = {
        multiplePhotoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    },
        modifier = Modifier
            .padding(top = 24.dp)
    ) {
        Text("Pick Images")
    }
}


//@Composable
//fun DetailPrice(state: TransactionState) {
////    var expanded by remember { mutableStateOf() }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputPersonalData(viewModel: TransactionViewModel,state: TransactionState) {
    Column {
        TextField(value = state.name ?: "",
            label = {Text(text = "Name")},
            onValueChange = {name ->
                viewModel.onNameChange(name)
        })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = state.noTelephone ?: "",
            label = {Text(text = "Contact")},
            onValueChange = {param ->
                viewModel.onNoTelpChange(param)
            })
        Spacer(modifier = Modifier.height(8.dp))
        DropDownGender(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = state.job ?: "",
            label = {Text(text = "Job")},
            onValueChange = {param ->
                viewModel.onJobChange(param)
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownGender(viewModel: TransactionViewModel,state: TransactionState) {
    val coffeeDrinks = arrayOf("Laki-Laki", "Perempuan", "Renfred")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            viewModel.onGenderChange(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RequestCard(item: ContentModel) {
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
                Text(
                    text = item.type.toString(),
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                
            }
        }
    }
}
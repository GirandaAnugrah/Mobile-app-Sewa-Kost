package com.example.uas_koskosan_kelompok5.view.service

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import com.example.uas_koskosan_kelompok5.state.ContentState
import com.example.uas_koskosan_kelompok5.viewmodel.ContentViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.uas_koskosan_kelompok5.view.component.ProgressLoadingScreen


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun UploadScreen(
    state: ContentState,
    viewModel: ContentViewModel,
    onSubmitContent: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(enabled = true, state = scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val multiplePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = {
                viewModel.onImagesChange(it)
            }
        )
        LazyRow{
            items(state.images ?: emptyList()){ uri ->
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
            Text("Pick Multiple Images")
        }
        OutlinedTextField(value = state.title.orEmpty(),
            onValueChange = {title ->
                viewModel.onTitleChange(title)
            },
            label = {
                Text(text = stringResource(id = R.string.content_title))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 0.dp, end = 0.dp, bottom = 0.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.content_facility),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Start)
        
        ListFacilities(viewModel = viewModel,state = state)
        Spacer(modifier = Modifier.height(16.dp))
        AddressScreen(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(16.dp))
        TelpScreen(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(16.dp))
        PriceScreen(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(16.dp))
        DropdownMenuExample(viewModel = viewModel)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.description.orEmpty(),
            onValueChange = {desc ->
                viewModel.onDescriptionChange(desc)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = LocalDensity.current.run { 16.sp }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            label = { Text("Enter your text here") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProgressLoadingScreen(isLoading = isLoading)

        Button(onClick = {
            isLoading = true
            onSubmitContent()
        },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.submit),fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp)
        }


    }


}


@Composable
fun ListFacilities(viewModel: ContentViewModel,state: ContentState) {
    var check = viewModel.checkboxItems
    var listRoomFacility by remember {
        mutableStateOf(check.toMutableList())
    }

//    val checkboxItems = viewModel.checkboxItems

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_bolt),
                    contentDescription = "Electricity",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Electricity")
                Checkbox(
                    checked = state.electricity ,
                    onCheckedChange = { newCheckState ->
                        viewModel.onElectricityChange(newCheckState)
                    }
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_bed),
                    contentDescription = "Bed",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Bed")
                Checkbox(
                    checked = state.bed ,
                    onCheckedChange = { newCheckState ->
                        viewModel.onBedChange(newCheckState)
                    }
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_desk),
                    contentDescription = "Desk",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Desk")
                Checkbox(
                    checked = state.desk ,
                    onCheckedChange = { newCheckState ->
                        viewModel.onDeskChange(newCheckState)
                    }
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_cupboard),
                    contentDescription = "Cupboard",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Cupboard")
                Checkbox(
                    checked = state.cupboard ,
                    onCheckedChange = { newCheckState ->
                        viewModel.onCupboardChange(newCheckState)
                    }
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_pillow),
                    contentDescription = "Pillow",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Pillow")
                Checkbox(
                    checked = state.pillow ,
                    onCheckedChange = { newCheckState ->
                        viewModel.onPillowChange(newCheckState)
                    }
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_chair),
                    contentDescription = "Chair",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Chair")
                Checkbox(
                    checked = state.chair ,
                    onCheckedChange = { newCheckState ->
                        viewModel.onChairChange(newCheckState)
                    }
                )
            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(viewModel: ContentViewModel,state: ContentState) {
    OutlinedTextField(value = state.address.orEmpty(),
        onValueChange = {viewModel.onAddressChange(it)},
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Address")
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelpScreen(viewModel: ContentViewModel,state: ContentState) {
    OutlinedTextField(value = state.telp.orEmpty(),
        onValueChange = {viewModel.onTelpChange(it)},
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Contacts")
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceScreen(viewModel: ContentViewModel,state: ContentState) {
    OutlinedTextField(value = state.price.toString(),
        onValueChange = {viewModel.onPriceChange(it)},
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(text = "Price")
        })
}

@Composable
fun DropdownMenuExample(viewModel: ContentViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Select Item") }
    val items = listOf("Wanita", "Laki-Laki", "Campuran")

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Text(text)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.onTypeChange(item)
                        text = item
                        expanded = false
                    },
                    text = { Text(text = item) }
                ) 
            }
        }
    }
}

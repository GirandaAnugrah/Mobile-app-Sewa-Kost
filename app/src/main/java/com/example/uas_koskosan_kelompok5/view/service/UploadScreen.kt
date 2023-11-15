package com.example.uas_koskosan_kelompok5.view.service

import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_koskosan_kelompok5.MainActivity
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun UploadScreen(
    onSubmitContent: (title : String?, facilities : List<String>?, desc: String?) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var bedroom by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }

    var listRoomFacility by remember {
        mutableStateOf(
            mutableListOf(
                "Electricity",
                "Kasur",
                "Meja",
                "lemari",
                "Bantal",
                "Kursi"
            )
        )
    }

    var myRoomFacility by remember {
        mutableStateOf(
            mutableListOf(
                false,false,false,false,false,false
            )
        )
    }
    val selectedFacilities = remember(myRoomFacility) {
        listRoomFacility.zip(myRoomFacility) { facility, isSelected ->
            if (isSelected) facility else null
        }.filterNotNull()
    }

    Column {
//        Image(painter = painterResource(id = R.drawable.apartement_sample), contentDescription = null)
        TextField(value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text(text = stringResource(id = R.string.content_title))
            })
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.content_facility))

        Column {
            myRoomFacility.forEachIndexed { index, isChecked ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        Text(text = listRoomFacility[index])
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { newCheckState ->
                                myRoomFacility = myRoomFacility.toMutableList().also {
                                    it[index] = newCheckState
                                }
//                                myRoomFacility[index] = newCheckState
                            }
                        )
                    }
                )
            }
        }



        TextField(
            value = desc,
            onValueChange = {
                desc = it
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


        Button(onClick = {
            onSubmitContent(title,selectedFacilities,desc)
        }) {
            Text(text = stringResource(id = R.string.submit))
        }

    }
}




@Preview(showBackground = true)
@Composable
fun UploadPreview() {
//    UploadScreen()
}
package com.example.uas_koskosan_kelompok5.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_SIGNUP


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController,
                onSignIn: (email : String?, password : String?) -> Unit,
                error: String?) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf("") }
    
    Column {
        if(isError){
            Text(text = messageError)
        }
        if(error!!.isNotBlank()){
            error?.let {
                Text(text = it)
            }
        }
        TextField(value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = stringResource(id = R.string.email_field))
            })

        TextField(value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = stringResource(id = R.string.password_field))
            },
            visualTransformation = PasswordVisualTransformation())

        Button(onClick = {
//            signIn
            if(email.isBlank() || password.isBlank()){
                isError = true
                messageError = "Please Insert data"
            }else{
                onSignIn(email,password)
            }
        }) {
            Text(text = stringResource(id = R.string.login))
        }

        Row {
            Text(text = "Not Registered")
            Text(text = stringResource(id = R.string.sign_up),
                modifier = Modifier.clickable {
                    navController.navigate(ROUTE_SIGNUP)
                })
        }
    }

}

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun LoginScreenPreviewLight() {
//    UAS_KosKosan_Kelompok5Theme {
//        LoginScreen(rememberNavController(),null)
//    }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun LoginScreenPreviewDark() {
//    UAS_KosKosan_Kelompok5Theme {
//        LoginScreen(rememberNavController())
//    }
//}

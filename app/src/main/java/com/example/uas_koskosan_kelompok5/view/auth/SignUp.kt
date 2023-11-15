package com.example.uas_koskosan_kelompok5.view.auth


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_LOGIN
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_SIGNUP
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.example.uas_koskosan_kelompok5.viewmodel.AuthViewModel
import java.util.regex.Pattern


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    onSignUp: (username : String?, email : String?, password : String?, isSeller: Boolean) -> Unit,
    error: String?
) {
    val authViewModel = viewModel<AuthViewModel>()

    var username by remember { mutableStateOf("") }
    var isUsernameError by remember { mutableStateOf(false) }
    var messageusernameError by remember { mutableStateOf("") }
    
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var messageEmailError by remember { mutableStateOf("") }
    
    var password by remember { mutableStateOf("") }
    var secondPassword by remember { mutableStateOf("") }
    
    var isErrorPassword by remember { mutableStateOf(false) }
    var messagepasswordNotSame by remember { mutableStateOf("") }

    var isSeller by remember { mutableStateOf(false) }


    Column {

        TextField(value = username,
            onValueChange = {
                username = it
                isUsernameError = false
            },
            label = {
                Text(text = stringResource(id = R.string.username_field))
            },
            isError = isUsernameError)
        if (isUsernameError){
            Text(text = messageusernameError)
        }

        TextField(value = email,
            onValueChange = {
                email = it
                isEmailError = false
            },
            label = {
                Text(text = stringResource(id = R.string.email_field))
            },
            isError = isEmailError)
        if(error!!.isNotBlank()){
            error?.let {
                Text(text = it)
            }
        }
        if (isEmailError){
            Text(text = messageEmailError)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSeller,
                onCheckedChange = { isSeller = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Blue,
                    uncheckedColor = Color.Gray
                )
            )
            Text(
                text = "Do you want to be seller",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        TextField(value = password,
            onValueChange = {
                password = it
                isErrorPassword = false
            },
            label = {
                Text(text = stringResource(id = R.string.password_field))
            },
            isError = isErrorPassword)
        if (isErrorPassword){
            Text(text = messagepasswordNotSame)
        }

        TextField(value = secondPassword,
            onValueChange = {
                secondPassword = it
                isErrorPassword = false
            },
            label = {
                Text(text = stringResource(id = R.string.confirm_field))
            },
            isError = isErrorPassword)
        if (isErrorPassword){
            Text(text = messagepasswordNotSame)
        }

        Button(onClick = {
            if(username.isBlank() || email.isBlank() || password.isBlank() || secondPassword.isBlank()){
                if(username.isBlank()){
                    isUsernameError = true
                }else if(email.isBlank()){
                    isEmailError = true
                }else if(password.isBlank() || secondPassword.isBlank()){
                    isErrorPassword  = true
                }
                messageusernameError = "This Field Cannot Blank"
                messageEmailError = "This Field Cannot Blank"
                messagepasswordNotSame = "This Field Cannot Blank"
            }else if(!isEmailValid(email)){
                isEmailError = true
                messageEmailError = "Please Insert Valid Email"
            } else if(password.length < 8){
                isErrorPassword = true
                messagepasswordNotSame = "Insert Minimum 8 characters"
            }else if(password != secondPassword){
                isErrorPassword = true
                messagepasswordNotSame = "Password does not match"
                password = ""
                secondPassword = ""
            }else{
                onSignUp(username,email, password,isSeller)
            }
        }
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Row {
            Text(text = "Not Registered")
            Text(text = stringResource(id = R.string.sign_in),
                modifier = Modifier.clickable {
                    navController.navigate(ROUTE_LOGIN)
                })
        }
    }

}

fun isEmailValid(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val pattern = Pattern.compile(emailPattern)
    val matcher = pattern.matcher(email)
    return matcher.matches()
}


//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun SignUpScreenPreviewLight() {
//    UAS_KosKosan_Kelompok5Theme {
//        LoginScreen(rememberNavController())
//    }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun SignUpScreenPreviewDark() {
//    UAS_KosKosan_Kelompok5Theme {
//        LoginScreen(rememberNavController())
//    }
//}

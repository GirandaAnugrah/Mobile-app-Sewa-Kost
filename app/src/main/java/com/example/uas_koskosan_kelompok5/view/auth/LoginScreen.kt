package com.example.uas_koskosan_kelompok5.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_SIGNUP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onSignIn: (email: String?, password: String?) -> Unit,
    error: String?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isError) {
            Text(text = messageError, color = Color.Red)
        }
        if (error!!.isNotBlank()) {
            error?.let {
                Text(text = it, color = Color.Red)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.apartement_sample),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth() // Adjust the maximum width as needed
                .padding(bottom = 16.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Text(
            text = "LOGIN",
            style = LocalTextStyle.current.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = stringResource(id = R.string.email_field))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = stringResource(id = R.string.password_field))
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                // signIn
                if (email.isBlank() || password.isBlank()) {
                    isError = true
                    messageError = "Please Insert data"
                } else {
                    onSignIn(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .height(56.dp)
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Row {
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier
                    .clickable {
                        navController.navigate(ROUTE_SIGNUP)
                    }
                    .padding(top = 5.dp),
                style = LocalTextStyle.current.copy(
                    fontSize = 16.sp
                )
            )
        }
    }
}

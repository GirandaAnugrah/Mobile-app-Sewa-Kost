package com.example.uas_koskosan_kelompok5


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgs
import com.example.uas_koskosan_kelompok5.model.UserModel
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_HOME
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_LOGIN
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_SIGNUP
import com.example.uas_koskosan_kelompok5.view.auth.LoginScreen
import com.example.uas_koskosan_kelompok5.view.auth.SignUpScreen
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthenticationActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()

    private var TAG = "Authentication"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        setContent {
            UAS_KosKosan_Kelompok5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }



    @Composable
    fun AppNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = ROUTE_LOGIN
    ) {
        var messageError by remember { mutableStateOf("") }
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(ROUTE_LOGIN) {
                LoginScreen(
                    navController,
                    onSignIn = {email,password ->
                        lifecycleScope.launch {
                            if(!email.isNullOrBlank() && !password.isNullOrBlank()){
                                signIn(email, password, error = {errorMessage ->
                                    if(errorMessage != null) messageError = errorMessage
                                })
                            }
                        }
                    },
                    error = messageError)
            }
            composable(ROUTE_SIGNUP) {
                SignUpScreen(
                    navController,
                    onSignUp = {username, email,password, isSeller ->
                        lifecycleScope.launch {
                            if(!username.isNullOrBlank() && !email.isNullOrBlank() && !password.isNullOrBlank()){
                                signUp(username,email,password,isSeller, error = {errorMessage ->
                                    if(errorMessage != null) messageError = errorMessage
                                })
                            }
                        }
                    },
                    error = messageError)
            }
        }
    }


     private fun signUp(username: String, email:String,password:String,isSeller: Boolean,error: (errorMessage:String?) -> Unit){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
//                    val profileUpdate = UserProfileChangeRequest.Builder()
//                        .setDisplayName(username)
//                        .build()
//
//                    user?.updateProfile(profileUpdate)

                    val userIntoDatabase = UserModel(username,email,isSeller)
                    val pushData = database.reference.child("users").child(user!!.uid).push()
                    pushData.setValue(userIntoDatabase).addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            Toast.makeText(baseContext,"Insert data into firebase success",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(baseContext,"Insert data into firebase is not success",Toast.LENGTH_SHORT).show()
                        }
                    }

                    updateUI(user)
                } else {
                    error(task.exception?.message)
                }
            }
    }

    private fun signIn(email:String,password:String,error: (errorMessage:String?) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    val message = "Invalid Email & Password"
                    error(message)
                }
            }
    }

//    public override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
            finish()
        }else{
            Toast.makeText(baseContext,"Error 500 Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        public val USER_NAME = "Username"
    }
}
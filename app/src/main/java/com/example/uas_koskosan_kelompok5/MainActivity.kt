package com.example.uas_koskosan_kelompok5

import android.annotation.SuppressLint
import android.app.DownloadManager.Query
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.uas_koskosan_kelompok5.ui.theme.UAS_KosKosan_Kelompok5Theme
//import android.os.Bundle
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
//import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uas_koskosan_kelompok5.navigation.Screen
import com.example.uas_koskosan_kelompok5.view.BookmarkView
import com.example.uas_koskosan_kelompok5.view.ChatView
import com.example.uas_koskosan_kelompok5.view.HomeView
import com.example.uas_koskosan_kelompok5.view.ProfileView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import com.example.uas_koskosan_kelompok5.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser
    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        var authenticationIntent = Intent(this, AuthenticationActivity::class.java)
        FirebaseApp.initializeApp(this)
//        searchuser(currentUser)
        super.onCreate(savedInstanceState)
        setContent {
            UAS_KosKosan_Kelompok5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashBoardDefaut(currentUser,authenticationIntent)
                }
            }
        }
    }

//    private fun searchuser(currentUser: FirebaseUser?){
//        val query = database.reference.child("users").child(currentUser!!.uid).child("email").equalTo(currentUser.email)
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Handle the data here
//                for (userSnapshot in dataSnapshot.children) {
//                    val user = userSnapshot.getValue(UserModel::class.java)
//                    Log.d("CURRENTUSER",user.toString())
//                    // Do something with the user object
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle errors
//                Log.d("CURRENTUSER_ERROR",databaseError.message)
//            }
//        })
//    }

    data class BottomNavigationItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unSelectIcon: ImageVector,
        val itemRoute: String
    )


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
    @Composable
    fun DashBoardDefaut(currentUser: FirebaseUser?, auth: Intent) {
        val navController = rememberNavController()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(Screen.HomeScreen.route)
        }
        val items = listOf(
            BottomNavigationItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unSelectIcon = Icons.Outlined.Home,
                itemRoute = Screen.HomeScreen.route
            ),
            BottomNavigationItem(
                title = "Chat",
                selectedIcon = Icons.Filled.Email,
                unSelectIcon = Icons.Outlined.Email,
                itemRoute = Screen.ChatScreen.route
            ),
            BottomNavigationItem(
                title = "Bookmark",
                selectedIcon = Icons.Filled.Email,
                unSelectIcon = Icons.Outlined.Email,
                itemRoute = Screen.BookmarkScreen.route
            ),
            BottomNavigationItem(
                title = "Profile",
                selectedIcon = Icons.Filled.AccountCircle,
                unSelectIcon = Icons.Outlined.AccountCircle,
                itemRoute = Screen.ProfileScreen.route
            ),
        )
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val currentRoute = selectedItemIndex
                    items.forEach {item ->
                        NavigationBarItem(
                            selected = currentRoute == item.itemRoute,
                            onClick = {
                                selectedItemIndex = item.itemRoute
                                if (currentRoute != item.itemRoute) {
                                    navController.navigate(item.itemRoute) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
//                                    if(item.badgeCount != null) {
//                                        Badge {
//                                            Text(text = item.badgeCount.toString())
//                                        }
//                                    } else if(item.hasNews) {
//                                        Badge()
//                                    }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (currentRoute == item.itemRoute) {
                                            item.selectedIcon
                                        } else item.unSelectIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ){
            NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
                composable(route = Screen.HomeScreen.route){
                    HomeView(navController = navController)
                }
                composable(route = Screen.ChatScreen.route){
//                ChatView(navController = navController)
                    ChatView()
                }
                composable(route = Screen.BookmarkScreen.route){
                    BookmarkView()
//                mainScreen(navController)
                }
                composable(route = Screen.ProfileScreen.route){
                    if(currentUser !== null){
                        ProfileView(navController = navController,currentUser, onRefresh = {
                            refreshActivity()
                        })
                    }else{
                        ProfileViewUserNotLogin(auth)
                    }
                }

            }

        }
    }


    private fun refreshActivity() {
        val intent = getIntent()
        Firebase.auth.signOut()
        finish()
        startActivity(intent)
    }

    @Composable
    fun ProfileViewUserNotLogin(auth: Intent) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Text("Profile Screen",)

                Button(onClick = {
                    startActivity(auth)
                }) {
                    Text(text = "Login as owner")
                }
                Button(onClick = {
                    startActivity(auth)
                }) {
                    Text(text = "Login as buyer")
                }
            }
        }
    }

}








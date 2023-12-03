package com.example.uas_koskosan_kelompok5

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseRealtimeService
import com.example.uas_koskosan_kelompok5.firebaseService.FirebaseStorageService
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.service.ContentService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.uas_koskosan_kelompok5.view.content.DetailsScreen

class MainActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser

    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        val contentService = ContentService(FirebaseRealtimeService, FirebaseStorageService)
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
                    DashBoardDefaut(currentUser,authenticationIntent,contentService)
                }
            }
        }
    }

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
    fun DashBoardDefaut(currentUser: FirebaseUser?, auth: Intent, contentService: ContentService) {
        val navController = rememberNavController()

//        var contentsData by remember { mutableStateOf<List<ContentModel>?>(null) }
        val contentsData = remember { mutableStateOf<List<ContentModel>>(emptyList()) }

        var contentData by remember { mutableStateOf<ContentModel?>(null) }

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
                    LaunchedEffect(contentData) {
                        contentService.getData(contentsData)
                    }
                    HomeView(navController = navController,contentsData.value,
                        navigateToDetails = {id ->
                            lifecycleScope.launch {
                                try {
                                    val result = contentService.getContentById(id)
                                    if (result.data == null) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Item Not FOund",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate(Screen.HomeScreen.route)
                                    } else {
                                        Log.d("DETAILCONTENT","Parsing data")
                                        contentData = result.data
                                    }
                                } catch (e: Exception) {
                                    Log.e("DETAILS", e.toString())
                                }
                                if(contentData != null) {
                                    navController.navigate("content_detail/${id}")
                                }
                            }
                        })
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
                        }, onCreateNewContent = {
                            intoServiceActivity()
                        })
                    }else{
                        ProfileViewUserNotLogin(auth)
                    }
                }
                composable(route = "content_detail/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        }
                    )){
                    Log.d("DETAILCONTENT","MASUK DETAIL CONTENT")

                    contentData?.let { it1 -> DetailsScreen(item = it1,
                        firebaseUser = currentUser,
                        deleteContent = {id ->
                            lifecycleScope.launch {
                                try {
                                    contentService.deleteContent(id)
                                    Toast.makeText(
                                        applicationContext,
                                        "Delete Data Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(Screen.HomeScreen.route)
                                }catch (e: Exception){
                                    Toast.makeText(
                                        applicationContext,
                                        "Cannot Delete Data",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        updateContent = {id ->
                            lifecycleScope.launch {
                                intoServiceActivityUpdate(id)
//                                try {
//                                    val result = contentService.getContentById(id)
//                                    if (result.data == null) {
//                                        Toast.makeText(
//                                            applicationContext,
//                                            "Item Not Found",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                        navController.navigate(Screen.HomeScreen.route)
//                                    } else {
//                                        Log.d("DETAILCONTENT","Parsing data")
//                                        contentData = result.data
//                                    }
//                                }catch (e: Exception){
//                                    Toast.makeText(
//                                        applicationContext,
//                                        "Something went wrong error 400",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                                if(contentData != null) {
//                                    navController.navigate("update/${id}")
//                                }
                            }
                        })
                    }
                }
                composable(route = "update/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        }
                    )){


//                    BookmarkView()
//                mainScreen(navController)
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

    private fun intoServiceActivity() {
        val intent = Intent(this, ServiceActivity::class.java)
        startActivity(intent)
    }

    private fun intoServiceActivityUpdate(id: String) {
        val intent = Intent(this, ServiceActivity::class.java)
        intent.putExtra("ID",id)
//        intent.putExtra("ISUPDATE", true)
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








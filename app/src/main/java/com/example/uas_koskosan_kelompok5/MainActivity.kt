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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.model.BookmarkModel
import com.example.uas_koskosan_kelompok5.model.TransactionModel
import com.example.uas_koskosan_kelompok5.service.CartService
import com.example.uas_koskosan_kelompok5.service.TransactionService
import com.example.uas_koskosan_kelompok5.view.MyKostScreen
import com.example.uas_koskosan_kelompok5.view.TransactionScreen
import com.example.uas_koskosan_kelompok5.view.content.DetailsScreen
import com.example.uas_koskosan_kelompok5.view.transaction.DetailTransactionCustomer
import java.util.UUID

class MainActivity : ComponentActivity() {
    private var currentUser = Firebase.auth.currentUser

    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        val contentService = ContentService(FirebaseRealtimeService, FirebaseStorageService)
        val cartService = CartService(FirebaseRealtimeService)
        val transactionService = TransactionService(FirebaseRealtimeService,FirebaseStorageService)
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
//                    NavbarApp()
                    DashBoardDefaut(currentUser,authenticationIntent,contentService,cartService,transactionService)
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
    fun DashBoardDefaut(currentUser: FirebaseUser?, auth: Intent, contentService: ContentService, cartService: CartService,transactionService: TransactionService) {
        val navController = rememberNavController()

//        var contentsData by remember { mutableStateOf<List<ContentModel>?>(null) }
        val contentsData = remember { mutableStateOf<List<ContentModel>>(emptyList()) }

        var contentData by remember { mutableStateOf<ContentModel?>(null) }

        val listBookmarks = remember { mutableStateOf<List<BookmarkModel>>(emptyList()) }
        val listTransactions = remember { mutableStateOf<List<TransactionModel>>(emptyList()) }
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(Screen.HomeScreen.route)
        }
        var isSellerArray : List<String>?
        var isSeller: Boolean? = false
        if(!currentUser?.uid.isNullOrBlank()){
            isSellerArray = currentUser?.displayName?.split(" ")
            isSeller = isSellerArray!![isSellerArray.size - 1] == stringResource(id = R.string.isSeller)
        }
//        val isSeller = true
        val items = listOf(
            BottomNavigationItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unSelectIcon = Icons.Outlined.Home,
                itemRoute = Screen.HomeScreen.route
            ),
            BottomNavigationItem(
                title = "Transaction",
                selectedIcon = Icons.Filled.List,
                unSelectIcon = Icons.Outlined.List,
                itemRoute = Screen.ChatScreen.route
            ),
            BottomNavigationItem(
                title = "My Kost",
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
            topBar = {
                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "REKOST",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    // Tampilin profile user
                    AsyncImage(
                        model = currentUser?.photoUrl,
                        contentDescription = "Profile User",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            },
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
                    HomeView(navController = navController,
                        currentUser = currentUser,
                        contentsData.value,
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
                                    intoDetailActivity(id)
//                                    navController.navigate("content_detail/${id}")
                                }
                            }
                        })
                }
                composable(route = Screen.ChatScreen.route){

                    LaunchedEffect(listTransactions) {
                        if (currentUser != null && !isSeller!!) {
                            transactionService.getDataFromCutomer(currentUser.uid, listTransactions)
                        }else if (currentUser != null && isSeller == true){
                            transactionService.getDataFromSeller(currentUser.uid, listTransactions)
                        }

                    }
                    TransactionScreen(
                        items = listTransactions.value,
                        intoDetailTransaction = {id ->
                            lifecycleScope.launch {
                                if (isSeller == true){
                                    intoTransactionSellerActivity(id)
                                }else{
                                    intoTransactionCustomerActivity(id)
                                }
                            }
                        }
                    )
                }
                composable(route = Screen.BookmarkScreen.route){
                    LaunchedEffect(listTransactions) {
                        if (currentUser != null && !isSeller!!) {
                            transactionService.getDataFromCutomer(currentUser.uid, listTransactions)
                        }else if (currentUser != null && isSeller == true){
                            transactionService.getDataFromSeller(currentUser.uid, listTransactions)
                        }

                    }
                    MyKostScreen(
                        items = listTransactions.value,
                        intoDetailTransaction = {id ->
                            lifecycleScope.launch {
                                if (isSeller == true){
                                    intoTransactionSellerActivity(id)
                                }else{
                                    intoTransactionCustomerActivity(id)
                                }
                            }
                        }
                    )

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
//                Section Detail
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

    private fun intoDetailActivity(id: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ID",id)
        startActivity(intent)
    }

    private fun intoTransactionSellerActivity(id: String) {
        val intent = Intent(this, SellerTransactionActivity::class.java)
        intent.putExtra("TRANSACTION",id)
        startActivity(intent)
    }
    private fun intoTransactionCustomerActivity(id: String) {
        val intent = Intent(this,CustomerTransactionActivity::class.java)
        intent.putExtra("TRANSACTION",id)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
            ) {
                Button(
                    onClick = { startActivity(auth) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Login as owner",
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = { startActivity(auth) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Login as buyer",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


}








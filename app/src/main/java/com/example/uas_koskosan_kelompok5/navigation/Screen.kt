package com.example.uas_koskosan_kelompok5.navigation

sealed class Screen(val route: String){
    object HomeScreen: Screen("home_screen")
    object ChatScreen: Screen("chat_screen")
    object BookmarkScreen: Screen("bookmark_screen")
    object ProfileScreen: Screen("profile_screen")
}

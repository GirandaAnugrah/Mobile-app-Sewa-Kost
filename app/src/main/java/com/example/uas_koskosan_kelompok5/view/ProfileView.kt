package com.example.uas_koskosan_kelompok5.view

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uas_koskosan_kelompok5.model.UserModel
import com.example.uas_koskosan_kelompok5.navigation.ROUTE_HOME
import com.example.uas_koskosan_kelompok5.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log

@Composable
fun ProfileView(
    navController: NavController, firebaseUser: FirebaseUser?,onRefresh: () -> Unit
) {
//    searchuser(firebaseUser)
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

            Button(onClick = { onRefresh() }) {
                Text(text = "Logout")
            }
        }
    }
}

    private fun searchuser(currentUser: FirebaseUser?){
        val database = FirebaseDatabase.getInstance()
        val query = database.reference.child("users").child(currentUser!!.uid).child("email").equalTo(currentUser.email)
        Log.d("CURRENTUSER",query.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle the data here
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    Log.d("CURRENTUSER",user.toString())
                    // Do something with the user object
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Log.d("CURRENTUSER_ERROR",databaseError.message)
            }
        })
    }


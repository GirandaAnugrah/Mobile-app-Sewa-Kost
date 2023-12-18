package com.example.uas_koskosan_kelompok5.view

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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.uas_koskosan_kelompok5.R
import com.example.uas_koskosan_kelompok5.handleImage.LoadImage

@Composable
fun ProfileView(
    navController: NavController, firebaseUser: FirebaseUser?,onRefresh: () -> Unit,onCreateNewContent: () -> Unit
) {
    val isSellerArray = firebaseUser?.displayName?.split(" ")
    val isSeller = isSellerArray!![isSellerArray.size - 1] == stringResource(id = R.string.isSeller)

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            ImageProfile(img = firebaseUser.photoUrl.toString())
            DisplayName(username = firebaseUser.displayName.toString())
            DisplayEmail(email = firebaseUser.email.toString())
            Button(
                onClick = { onRefresh() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
            if (isSeller){
                Button(
                    onClick = { onCreateNewContent() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "New Content",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ImageProfile(img: String) {
    AsyncImage(model = img,
        contentDescription = img,
        modifier = Modifier
            .size(120.dp)
            .clip(shape = CircleShape),
        contentScale = ContentScale.Crop)
}

@Composable
fun DisplayName(username: String) {
    Text(
        text = username,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun DisplayEmail(email: String) {
    Text(text = email,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface)
}

//    private fun searchuser(currentUser: FirebaseUser?){
//        val database = FirebaseDatabase.getInstance()
//        val query = database.reference.child("users").child(currentUser!!.uid).child("email").equalTo(currentUser.email)
//        Log.d("CURRENTUSER",query.toString())
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


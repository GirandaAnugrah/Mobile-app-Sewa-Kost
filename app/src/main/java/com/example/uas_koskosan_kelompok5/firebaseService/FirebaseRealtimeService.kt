package com.example.uas_koskosan_kelompok5.firebaseService

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

object FirebaseRealtimeService {
    private val databaseRef: DatabaseReference by lazy {
        Firebase.database.reference
    }
    fun getReferenceChild(key : String): DatabaseReference {
        return databaseRef.child(key)
    }
}
package com.example.uas_koskosan_kelompok5.data

interface AuthRepository {
    suspend fun signIn(email:String,password:String)
    suspend fun signUp(emai:String,password: String)
}
package com.example.uas_koskosan_kelompok5.viewmodel


import androidx.lifecycle.ViewModel
import com.example.uas_koskosan_kelompok5.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class UserState(
    val username:String? = null,
    val email: String? = null,
    val isSeller: Boolean? = null
)
class AuthViewModel: ViewModel() {
    private val _state = MutableStateFlow(UserState())
    //Read Only state
    val state = _state.asStateFlow()


}
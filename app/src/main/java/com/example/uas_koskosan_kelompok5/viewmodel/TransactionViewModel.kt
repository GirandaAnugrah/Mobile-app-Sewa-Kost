package com.example.uas_koskosan_kelompok5.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.model.ContentModel
import com.example.uas_koskosan_kelompok5.model.TransactionData
import com.example.uas_koskosan_kelompok5.state.ContentState
import com.example.uas_koskosan_kelompok5.state.TransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TransactionViewModel: ViewModel() {
    private val _state = MutableStateFlow(TransactionState())
    val state = _state.asStateFlow()

    fun onCreateResult(result : TransactionData) {
        _state.update { it.copy(
            isCreatePostSuccessful = result.data != null,
            errorMessage = result.errorMessage
        ) }
    }
    fun onIdChange(param: String?){
        _state.update { it.copy(id = param ?: " ") }
    }

    fun onCustomerIdChange(param: String?){
        _state.update { it.copy(customerId = param ?: " ") }
    }

    fun onSellerIdChange(param: String?){
        _state.update { it.copy(sellerId = param ?: " ") }
    }

    fun onNameChange(param: String?){
        _state.update { it.copy(name = param ?: " ") }
    }

    fun onJobChange(param: String?){
        _state.update { it.copy(job = param ?: " ") }
    }

    fun onGenderChange(param: String?){
        _state.update { it.copy(gender = param ?: " ") }
    }

    fun onNoTelpChange(param: String?){
        _state.update { it.copy(noTelephone = param ?: " ") }
    }

    fun onImageChange(images: List<Uri>?){
        _state.update { it.copy(cardIdentity = images ?: emptyList()) }
    }

    fun onContentChange(param: ContentModel?){
        _state.update { it.copy(data = param) }
    }
    fun resetState() {
        _state.update { TransactionState() }
    }
}
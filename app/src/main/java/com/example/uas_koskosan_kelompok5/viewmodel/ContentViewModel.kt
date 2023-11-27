package com.example.uas_koskosan_kelompok5.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.uas_koskosan_kelompok5.model.ContentData
import com.example.uas_koskosan_kelompok5.state.ContentState
import com.example.uas_koskosan_kelompok5.utils.CheckBoxItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContentViewModel: ViewModel() {
    private val _state = MutableStateFlow(ContentState())
    val state = _state.asStateFlow()


    private val _checkboxItems = listOf<CheckBoxItems>(
        CheckBoxItems(1, title = "Electricity"),
        CheckBoxItems(2, title = "Kasur"),
        CheckBoxItems(3, title = "Meja"),
        CheckBoxItems(4, title = "Lemari"),
        CheckBoxItems(5, title = "Bantal"),
        CheckBoxItems(6, title = "Kursi"),
    )

    val checkboxItems: List<CheckBoxItems> = _checkboxItems


    fun onCreateResult(result : ContentData) {
        _state.update { it.copy(
            isCreatePostSuccessful = result.data != null,
            errorMessage = result.errorMessage
        ) }
    }

    fun onTitleChange(title: String?){
        _state.update { it.copy(title = title ?: " ") }
    }

    fun onDescriptionChange(desc: String?){
        _state.update { it.copy(description = desc ?: " ") }
    }

    fun onFeaturesChange(checkboxItem: MutableList<CheckBoxItems>, isChecked: Boolean, index: Int) {
        checkboxItem[index].isChecked = isChecked
        _state.update { it.copy(facilities = checkboxItems) }
    }

    fun onImagesChange(images: List<Uri>?){
        _state.update { it.copy(images = images ?: emptyList()) }
    }

    fun onAddressChange(param: String?){
        _state.update { it.copy(address = param ?: " ") }
    }

    fun onTypeChange(param: String?){
        _state.update { it.copy(type = param ?: " ") }
    }

    fun onTelpChange(param: String?){
        _state.update { it.copy(telp = param ?: " ") }
    }
    fun onPriceChange(param: String?){
        _state.update { it.copy(price = param?.toInt() ?: 0) }
    }

    fun resetState() {
        _state.update { ContentState() }
    }
//    CheckBoxItems(1, title = "Electricity"),
//    CheckBoxItems(2, title = "Kasur"),
//    CheckBoxItems(3, title = "Meja"),
//    CheckBoxItems(4, title = "Lemari"),
//    CheckBoxItems(5, title = "Bantal"),
//    CheckBoxItems(6, title = "Kursi"),
    fun onElectricityChange(param: Boolean?){
        _state.update { it.copy(electricity = param ?: false) }
    }

    fun onBedChange(param: Boolean?){
        _state.update { it.copy(bed = param ?: false) }
    }

    fun onDeskChange(param: Boolean?){
        _state.update { it.copy(desk = param ?: false) }
    }

    fun onCupboardChange(param: Boolean?){
        _state.update { it.copy(cupboard = param ?: false) }
    }

    fun onPillowChange(param: Boolean?){
        _state.update { it.copy(pillow = param ?: false) }
    }

    fun onChairChange(param: Boolean?){
        _state.update { it.copy(chair = param ?: false) }
    }
}
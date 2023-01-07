package com.yapp.gallery.home.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCompositionContext
import androidx.lifecycle.ViewModel
import com.yapp.gallery.domain.entity.home.ExhibitInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExhibitInfoViewModel @Inject constructor(

) : ViewModel(){
    private var _categoryList = MutableStateFlow(mutableListOf<String>())
    val categoryList : StateFlow<List<String>>
        get() = _categoryList

    private var _categoryState = MutableStateFlow<CategoryUiState>(CategoryUiState.Empty)
    val categoryState : StateFlow<CategoryUiState>
        get() = _categoryState

    private var _tempStorageList = mutableStateListOf<ExhibitInfo>()
    val tempStorageList : List<ExhibitInfo>
        get() = _tempStorageList

    init {
        // Todo : 임시 리스트
        _categoryList.value = mutableListOf("카테고리 1", "카테고리 2", "카테고리 3")
        _tempStorageList = mutableStateListOf(
            ExhibitInfo("전시명 1", "2023.01.06"),
            ExhibitInfo("전시명 2", "2023.01.06"),
            ExhibitInfo("전시명 3", "2023.01.06"),
        )
    }

    fun addCategory(category: String){
        _categoryList.value.add(category)
    }


    fun checkCategory(category: String){
        if (category in _categoryList.value){
            _categoryState.value = CategoryUiState.Error("이미 존재하는 카테고리입니다.")
        } else if (category.length > 20)
            _categoryState.value = CategoryUiState.Error("카테고리는 20자 이하이어야 합니다.")
        else if (category.isEmpty())
            _categoryState.value = CategoryUiState.Empty
        else
            _categoryState.value = CategoryUiState.Success
    }

    fun deleteTempStorageItem(index: Int){
        _tempStorageList.removeAt(index)
    }
}

sealed class CategoryUiState{
    object Success : CategoryUiState()
    object Empty : CategoryUiState()
    data class Error(val error: String) : CategoryUiState()
}
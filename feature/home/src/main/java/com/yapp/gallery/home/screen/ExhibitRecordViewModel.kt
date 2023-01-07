package com.yapp.gallery.home.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCompositionContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.home.ExhibitInfo
import com.yapp.gallery.domain.usecase.record.CreateCategoryUseCase
import com.yapp.gallery.domain.usecase.record.CreateRecordUseCase
import com.yapp.gallery.domain.usecase.record.GetCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitInfoViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val createRecordUseCase: CreateRecordUseCase
) : ViewModel(){
    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    private var _categoryState = MutableStateFlow<CategoryUiState>(CategoryUiState.Empty)
    val categoryState : StateFlow<CategoryUiState>
        get() = _categoryState

    private var _tempStorageList = mutableStateListOf<ExhibitInfo>()
    val tempStorageList : List<ExhibitInfo>
        get() = _tempStorageList

    init {
        getCategoryList()

        _tempStorageList = mutableStateListOf(
            ExhibitInfo("전시명 1", "2023.01.06"),
            ExhibitInfo("전시명 2", "2023.01.06"),
            ExhibitInfo("전시명 3", "2023.01.06"),
        )
    }

    private fun getCategoryList(){
        viewModelScope.launch {
            runCatching { getCategoryListUseCase() }
                .onSuccess { _categoryList.addAll(it) }
                .onFailure {  }
        }
    }

    fun addCategory(category: String){
        viewModelScope.launch {
            runCatching { createCategoryUseCase(category) }
                .onSuccess { _categoryList.add(CategoryItem(it, category)) }
                .onFailure {  }
        }
    }


    fun checkCategory(category: String){
        if (_categoryList.find { it.name == category } != null){
            _categoryState.value = CategoryUiState.Error("이미 존재하는 카테고리입니다.")
        } else if (category.length > 10)
            _categoryState.value = CategoryUiState.Error("카테고리는 10자 이하이어야 합니다.")
        else if (category.isEmpty())
            _categoryState.value = CategoryUiState.Empty
        else
            _categoryState.value = CategoryUiState.Success
    }

    fun deleteTempStorageItem(index: Int){
        _tempStorageList.removeAt(index)
    }

    fun createRecord(name: String, categoryId: Long, postDate: String) {
        viewModelScope.launch {
            runCatching { createRecordUseCase(name, categoryId, postDate) }
                .onSuccess {  }
                .onFailure {  }
        }
    }
}

sealed class CategoryUiState{
    object Success : CategoryUiState()
    object Empty : CategoryUiState()
    data class Error(val error: String) : CategoryUiState()
}
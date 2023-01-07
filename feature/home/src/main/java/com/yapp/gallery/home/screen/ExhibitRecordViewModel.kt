package com.yapp.gallery.home.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCompositionContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.home.ExhibitInfo
import com.yapp.gallery.domain.usecase.record.GetCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitInfoViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel(){
    private var _categoryList = MutableStateFlow(mutableListOf<CategoryItem>())
    val categoryList : StateFlow<List<CategoryItem>>
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
                .onSuccess { _categoryList.value = it as MutableList<CategoryItem> }
                .onFailure {  }
        }
    }

    fun addCategory(category: String){
        // Todo : 카테고리 생성 변경해야함
        _categoryList.value.add(CategoryItem(999, category))
    }


    fun checkCategory(category: String){
        if (_categoryList.value.find { it.name == category } != null){
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
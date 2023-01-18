package com.yapp.gallery.profile.screen.category

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.widget.model.CategoryUiState
import com.yapp.gallery.domain.entity.home.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManageViewModel @Inject constructor(

) : ViewModel(){

    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    private var _categoryState = MutableStateFlow<CategoryUiState>(CategoryUiState.Empty)
    val categoryState : StateFlow<CategoryUiState>
        get() = _categoryState

    private var _categoryManageState = MutableStateFlow<CategoryScreenState>(CategoryScreenState.Loading)
    val categoryManageState : StateFlow<CategoryScreenState>
        get() = _categoryManageState

    init {
        // Todo : 임시 카테고리 리스트
//        _categoryList.addAll(
//            listOf(CategoryItem(1, "카테고리 1"), CategoryItem(2, "카테고리 2"),
//                CategoryItem(3, "카테고리 3"), CategoryItem(4, "카테고리 4"),
//                CategoryItem(5, "카테고리 5")
//            )
//        )
        viewModelScope.launch {
            delay(2000)
            _categoryManageState.value = CategoryScreenState.Empty
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

    fun deleteCategory(category : CategoryItem){
        _categoryList.remove(category)
        if (_categoryList.isEmpty())
            _categoryManageState.value = CategoryScreenState.Empty
    }

    fun createCategory(categoryName: String){
        _categoryList.add(CategoryItem(1, categoryName))
        if (_categoryManageState.value is CategoryScreenState.Empty)
            _categoryManageState.value = CategoryScreenState.NotEmpty
    }

}

sealed class CategoryScreenState{
    object Loading : CategoryScreenState()
    object Empty : CategoryScreenState()
    object NotEmpty: CategoryScreenState()
}
package com.yapp.gallery.profile.screen.category

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.home.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryManageViewModel @Inject constructor(

) : ViewModel(){

    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    private var _categoryState = MutableStateFlow<BaseState<Boolean>>(BaseState.None)
    val categoryState : StateFlow<BaseState<Boolean>>
        get() = _categoryState

    private var _categoryManageState = MutableStateFlow<BaseState<Boolean>>(BaseState.Loading)
    val categoryManageState : StateFlow<BaseState<Boolean>>
        get() = _categoryManageState

    init {
        // Todo : 임시 카테고리 리스트
        _categoryList.addAll(
            listOf(CategoryItem(1, "카테고리 1", 1), CategoryItem(2, "카테고리 2", 3),
                CategoryItem(3, "카테고리 3", 2)
            )
        )
        _categoryManageState.value = BaseState.Success(true)
    }
    fun checkCategory(category: String){
        if (_categoryList.find { it.name == category } != null){
            _categoryState.value = BaseState.Error("이미 존재하는 카테고리입니다.")
        } else if (category.length > 10)
            _categoryState.value = BaseState.Error("카테고리는 10자 이하이어야 합니다.")
        else
            _categoryState.value = BaseState.Success(category.isNotEmpty())

    }

    fun deleteCategory(category : CategoryItem){
        _categoryList.remove(category)
        if (_categoryList.isEmpty())
            _categoryManageState.value = BaseState.Success(false)
    }

    fun createCategory(categoryName: String){
        _categoryList.add(CategoryItem(5, categoryName, 1))
        _categoryManageState.value = BaseState.Success(true)
    }

    fun reorderItem(from: Int, to: Int){
        _categoryList.add(to, _categoryList.removeAt(from))
    }

}

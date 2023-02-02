package com.yapp.gallery.profile.screen.category

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.profile.EditCategoryUseCase
import com.yapp.gallery.domain.usecase.record.GetCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManageViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val editCategoryUseCase: EditCategoryUseCase
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
        getCategoryList()
    }

    private fun getCategoryList(){
        viewModelScope.launch {
            getCategoryListUseCase()
                .catch { _categoryManageState.value = BaseState.Error(it.message) }
                .collect{
                    _categoryList.addAll(it)
                    _categoryManageState.value = BaseState.Success(it.isNotEmpty())
                }
        }

    }
    fun checkCategory(category: String){
        if (_categoryList.find { it.name == category } != null){
            _categoryState.value = BaseState.Error("이미 존재하는 카테고리입니다.")
        } else if (category.length > 20)
            _categoryState.value = BaseState.Error("카테고리는 20자 이하이어야 합니다.")
        else
            _categoryState.value = BaseState.Success(category.isNotEmpty())
    }

    fun editCategory(category: CategoryItem, editedName: String){
        viewModelScope.launch {
            editCategoryUseCase(category.id, editedName)
                .catch {  }
                .collectLatest {
                    Log.e("카테고리 편집", it)
                }
        }
        _categoryList[_categoryList.indexOf(category)] =
            CategoryItem(category.id,editedName, category.sequence)
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

    fun checkEditable(originCategory: String, category: String) {
        if (category == originCategory)
            _categoryState.value = BaseState.None
        else if (_categoryList.find { it.name == category } != null)
            _categoryState.value = BaseState.Error("이미 존재하는 카테고리입니다.")
        else if (category.length > 20)
            _categoryState.value = BaseState.Error("카테고리는 20자 이하이어야 합니다.")
        else
            _categoryState.value = BaseState.Success(category.isNotEmpty())
    }

    fun reorderItem(from: Int, to: Int){
        _categoryList.add(to, _categoryList.removeAt(from))
    }

}

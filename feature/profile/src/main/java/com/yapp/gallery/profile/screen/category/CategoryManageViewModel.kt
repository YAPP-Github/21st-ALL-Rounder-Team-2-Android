package com.yapp.gallery.profile.screen.category

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.profile.EditCategoryUseCase
import com.yapp.gallery.domain.usecase.record.GetCategoryListUseCase
import com.yapp.gallery.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryManageViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val editCategoryUseCase: EditCategoryUseCase
) : ViewModel(){

    // 카테고리 리스트 상태
    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    // 카테고리 자체 상태
    private var _categoryState = MutableStateFlow<BaseState<Boolean>>(BaseState.None)
    val categoryState : StateFlow<BaseState<Boolean>>
        get() = _categoryState

    // 카테고리 관리 스크린 상태
    private var _categoryManageState = MutableStateFlow<BaseState<Boolean>>(BaseState.Loading)
    val categoryManageState : StateFlow<BaseState<Boolean>>
        get() = _categoryManageState

    // 오류 발생한 경우
    private var _errorChannel = Channel<UiText>()
    val errors = _errorChannel.receiveAsFlow()

    init {
        getCategoryList()
    }

    private fun getCategoryList(){
        viewModelScope.launch {
            getCategoryListUseCase()
                .catch {
                    _categoryManageState.value = BaseState.Error(it.message)
                    _errorChannel.send(UiText.StringResource(R.string.category_list_error))
                }
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
                .catch {
                    _categoryState.value = BaseState.None
                    _errorChannel.send(UiText.StringResource(R.string.category_edit_error))
                }
                .collectLatest {
                    Log.e("카테고리 편집", it)
                    _categoryList[_categoryList.indexOf(category)] =
                        CategoryItem(category.id,editedName, category.sequence)
                }
        }
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

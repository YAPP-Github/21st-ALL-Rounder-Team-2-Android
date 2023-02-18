package com.yapp.gallery.home.screen.edit

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.record.CreateCategoryUseCase
import com.yapp.gallery.domain.usecase.record.GetCategoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase
) : ViewModel(){
    // Todo : 카테고리 조회 기능
    // Todo : 전시 삭제
    private val id : Long = checkNotNull(savedStateHandle["id"])

    val exhibitName = mutableStateOf(savedStateHandle["exhibitName"] ?: "")
    val categorySelect = mutableStateOf(savedStateHandle["categorySelect"] ?: -1L)
    val exhibitDate = mutableStateOf(savedStateHandle["exhibitDate"] ?: "")
    val exhibitLink = mutableStateOf(savedStateHandle["exhibitLink"] ?: "")

    private val _categoryState = MutableStateFlow<BaseState<Boolean>>(BaseState.Loading)
    val categoryState : StateFlow<BaseState<Boolean>>
        get() = _categoryState

    private val _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    private val _errors = Channel<UiText>()
    val errors = _errors.receiveAsFlow()

    init {
        getCategoryList()
    }

    private fun getCategoryList(){
        viewModelScope.launch {
            getCategoryListUseCase()
                .catch {

                }
                .collectLatest {
                    _categoryList.addAll(it)
                }
        }
    }

    fun addCategory(category: String){
        viewModelScope.launch {
            createCategoryUseCase(category)
                .catch {}
                .collect{
                    _categoryList.add(CategoryItem(it, category, _categoryList.size))
                }
        }
    }


    fun checkCategory(category: String){
        if (_categoryList.find { it.name == category } != null){
            _categoryState.value = BaseState.Error("이미 존재하는 카테고리입니다.")
        } else if (category.length > 10)
            _categoryState.value = BaseState.Error("카테고리는 10자 이하이어야 합니다.")
        else
            _categoryState.value = BaseState.Success(category.isNotEmpty())
    }
}
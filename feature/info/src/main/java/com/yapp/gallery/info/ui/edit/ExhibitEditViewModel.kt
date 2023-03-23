package com.yapp.gallery.info.ui.edit

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.edit.DeleteRemotePostUseCase
import com.yapp.gallery.domain.usecase.edit.UpdateRemotePostUseCase
import com.yapp.gallery.domain.usecase.record.CreateCategoryUseCase
import com.yapp.gallery.domain.usecase.record.GetCategoryListUseCase
import com.yapp.gallery.info.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val updateRemotePostUseCase: UpdateRemotePostUseCase,
    private val deleteRemotePostUseCase: DeleteRemotePostUseCase
) : ViewModel(){
    private val id : Long = checkNotNull(savedStateHandle["id"])

    val exhibitName = mutableStateOf(savedStateHandle["exhibitName"] ?: "")
    val categorySelect = mutableStateOf(savedStateHandle["categoryId"] ?: -1L)
    val exhibitDate = mutableStateOf(savedStateHandle["exhibitDate"] ?: "")
    val exhibitLink = mutableStateOf(savedStateHandle["exhibitLink"] ?: "")

    private val _editState = MutableStateFlow<ExhibitEditState>(ExhibitEditState.Initial)
    val editState : StateFlow<ExhibitEditState>
        get() = _editState

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
                    _errors.send(UiText.DynamicString("카테고리 조회에 실패하였습니다."))
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
                    _categoryList.add(CategoryItem(it, category, _categoryList.size, 0))
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

    fun updateRemotePost(){
        viewModelScope.launch {
            updateRemotePostUseCase(
                id, exhibitName.value, categorySelect.value,
                exhibitDate.value, exhibitLink.value.ifEmpty { null }
            ).catch {
                Log.e("updateError", it.message.toString())
                _editState.value =
                    ExhibitEditState.Error(UiText.StringResource(R.string.exhibit_update_error))
            }.collectLatest {
                _editState.value = ExhibitEditState.Update
            }
        }
    }
    fun deleteRemotePost(){
        viewModelScope.launch {
            deleteRemotePostUseCase(id)
                .catch {
                    _editState.value =
                        ExhibitEditState.Error(UiText.StringResource(R.string.exhibit_delete_error))
                }
                .collectLatest {
                    _editState.value = ExhibitEditState.Delete
                }
        }
    }
}
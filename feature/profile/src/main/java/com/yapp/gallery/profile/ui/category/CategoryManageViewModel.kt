package com.yapp.gallery.profile.ui.category

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.category.DeleteCategoryUseCase
import com.yapp.gallery.domain.usecase.category.EditCategorySequenceUseCase
import com.yapp.gallery.domain.usecase.category.EditCategoryUseCase
import com.yapp.gallery.domain.usecase.category.GetCategoryPostUseCase
import com.yapp.gallery.domain.usecase.record.CreateCategoryUseCase
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
    private val editCategoryUseCase: EditCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val changeSequenceUseCase: EditCategorySequenceUseCase,
    private val getCategoryPostUseCase: GetCategoryPostUseCase
) : ViewModel(){

    // 카테고리 리스트 상태
    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    // 카테고리 각각 상태
    private var _categoryPostList = mutableStateListOf<CategoryPostState>()
    val categoryPostState : List<CategoryPostState>
        get() = _categoryPostList

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
//                    _errorChannel.send(UiText.StringResource(R.string.category_list_error))
                }
                .collect{
                    _categoryList.addAll(it)
                    // 각 카테고리 상태 추가
                    it.forEach { category ->
                        _categoryPostList.add(CategoryPostState.Initial(category))
                    }
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
                    Log.e("category error", it.message.toString())
                    _errorChannel.send(UiText.StringResource(R.string.category_edit_error))
                }
                .collectLatest {
                    Log.e("카테고리 편집", it.toString())
                    if (it)
                        _categoryList[_categoryList.indexOf(category)] = CategoryItem(
                            category.id,editedName, category.sequence, category.postNum)
                    else
                        _errorChannel.send(UiText.StringResource(R.string.category_edit_error))
                }
        }
    }
    fun deleteCategory(category : CategoryItem){
        viewModelScope.launch {
            deleteCategoryUseCase(category.id)
                .catch {
                    _errorChannel.send(UiText.StringResource(R.string.category_delete_erorr))
                }
                .collectLatest {
                    if (it){
                        _categoryList.remove(category)
                        if (_categoryList.isEmpty())
                            _categoryManageState.value = BaseState.Success(false)
                    } else
                        _errorChannel.send(UiText.StringResource(R.string.category_delete_erorr))
                }
        }
    }

    fun createCategory(categoryName: String){
        viewModelScope.launch {
            createCategoryUseCase(categoryName)
                .catch {
                    _errorChannel.send(UiText.StringResource(R.string.category_add_error))
                }
                .collectLatest {
                    _categoryList.add(CategoryItem(it, categoryName, categoryList.size, 0).also { c ->
                        _categoryPostList.add(CategoryPostState.Initial(c))
                    })
                    _categoryManageState.value = BaseState.Success(true)
                }
        }
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
        // 달라진게 있으면 재정렬
        if (from != to){
            _categoryList.add(to, _categoryList.removeAt(from))
            _categoryPostList.add(to, _categoryPostList.removeAt(from))

            viewModelScope.launch {
                changeSequenceUseCase(_categoryList)
                    .catch {
                        _errorChannel.send(UiText.DynamicString(it.message.toString()))
                        _categoryList.add(from, _categoryList.removeAt(to))
                    }
                    .collectLatest { isSuccessful ->
                        if (isSuccessful){
                            _categoryList[from].sequence = _categoryList[to].sequence.also {
                                _categoryList[to].sequence = _categoryList[from].sequence
                            }
                        }
                        else{
                            _categoryList.add(from, _categoryList.removeAt(to))
                            _errorChannel.send(UiText.StringResource(R.string.category_swap_error))

                        }

                    }
            }
        }
    }

    // 카테고리 별 세부 전시 조회
    fun expandCategory(index: Int){
        when (_categoryPostList[index]){
            // 확장 하며 데이터 받아오기
            is CategoryPostState.Initial -> {
                val postId = (_categoryPostList[index] as CategoryPostState.Initial).categoryItem.id
                viewModelScope.launch {
                    // Todo : 추후 페이지 네이션 확장
                    getCategoryPostUseCase(postId = postId)
                        .catch {
                            Log.e("categoryPosterror", it.message.toString())
                            _errorChannel.send(UiText.StringResource(R.string.network_error))
                        }
                        .collectLatest {
                            _categoryPostList[index] = CategoryPostState.Expanded(it)
                        }
                }
            }
            // 단순 확장 Or not
            else ->{
                val postDetail = if (_categoryPostList[index] is CategoryPostState.Expanded)
                    (_categoryPostList[index] as CategoryPostState.Expanded).categoryPost
                else (_categoryPostList[index] as CategoryPostState.UnExpanded).categoryPost

                _categoryPostList[index] = if (_categoryPostList[index] is CategoryPostState.Expanded)
                    CategoryPostState.UnExpanded(postDetail)
                else CategoryPostState.Expanded(postDetail)
            }
        }
    }
}
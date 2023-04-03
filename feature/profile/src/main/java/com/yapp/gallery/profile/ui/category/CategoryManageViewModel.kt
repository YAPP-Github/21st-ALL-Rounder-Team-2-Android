package com.yapp.gallery.profile.ui.category

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yapp.gallery.common.base.BaseStateViewModel
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
import com.yapp.gallery.profile.ui.category.CategoryManageContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoryManageViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val editCategoryUseCase: EditCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val changeSequenceUseCase: EditCategorySequenceUseCase,
    private val getCategoryPostUseCase: GetCategoryPostUseCase
) : BaseStateViewModel<CategoryManageState, CategoryManageEvent, CategoryManageSideEffect>() {

    override val _viewState: MutableStateFlow<CategoryManageState> = MutableStateFlow(CategoryManageState.Initial)

    // 카테고리 리스트 상태
    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    // 카테고리 각각 상태
    private var _categoryPostStateList = mutableStateListOf<CategoryPostState>()
    val categoryPostStateList : List<CategoryPostState>
        get() = _categoryPostStateList

    // 카테고리 자체 상태
    private var _categoryState = MutableStateFlow<BaseState<Boolean>>(BaseState.None)
    val categoryState : StateFlow<BaseState<Boolean>>
        get() = _categoryState


    // 오류 발생한 경우
    private var _errorChannel = Channel<UiText>()
    val errors = _errorChannel.receiveAsFlow()

    init {
        getCategoryList()
    }

    private fun getCategoryList(){
        getCategoryListUseCase()
            .catch {
                setViewState(CategoryManageState.Error)
            }
            .onEach {
                _categoryList.addAll(it)
                // 각 카테고리 상태 추가
                it.forEach { category ->
                    _categoryPostStateList.add(
                        CategoryPostState.Initial(getCategoryPostUseCase(category.id).cachedIn(viewModelScope))
                    )
                }
                setViewState(CategoryManageState.Success)
            }
            .launchIn(viewModelScope)
    }
    private fun checkCategory(category: String){
        if (_categoryList.find { it.name == category } != null){
            _categoryState.value = BaseState.Error("이미 존재하는 카테고리입니다.")
        } else if (category.length > 20)
            _categoryState.value = BaseState.Error("카테고리는 20자 이하이어야 합니다.")
        else
            _categoryState.value = BaseState.Success(category.isNotEmpty())
    }

    private fun editCategory(category: CategoryItem, editedName: String){
        viewModelScope.launch {
            editCategoryUseCase(category.id, editedName)
                .catch {
                    Timber.tag("category error").e(it.message.toString())
                    _errorChannel.send(UiText.StringResource(R.string.category_edit_error))
                }
                .collectLatest {
                    Timber.tag("카테고리 편집").e(it.toString())
                    if (it)
                        _categoryList[_categoryList.indexOf(category)] = CategoryItem(
                            category.id, editedName, category.sequence, category.postNum
                        )
                    else
                        _errorChannel.send(UiText.StringResource(R.string.category_edit_error))
                }
        }
    }
    private fun deleteCategory(category : CategoryItem){
        deleteCategoryUseCase(category.id)
            .catch {
                _errorChannel.send(UiText.StringResource(R.string.category_delete_erorr))
            }
            .onEach {
                if (it){
                    _categoryList.remove(category)
                    if (_categoryList.isEmpty())
                        setViewState(CategoryManageState.Empty)
                } else
                    _errorChannel.send(UiText.StringResource(R.string.category_delete_erorr))
            }
            .launchIn(viewModelScope)
    }

    private fun createCategory(categoryName: String){
        createCategoryUseCase(categoryName)
            .catch {
                _errorChannel.send(UiText.StringResource(R.string.category_add_error))
            }
            .onEach {
                _categoryList.add(CategoryItem(it, categoryName, categoryList.size, 0).also { c ->
                    _categoryPostStateList.add(CategoryPostState.Initial(getCategoryPostUseCase(c.id).cachedIn(viewModelScope)))
                })
                setViewState(CategoryManageState.Success)
            }
            .launchIn(viewModelScope)
    }

    private fun checkEditable(originCategory: String, category: String) {
        if (category == originCategory)
            _categoryState.value = BaseState.None
        else if (_categoryList.find { it.name == category } != null)
            _categoryState.value = BaseState.Error("이미 존재하는 카테고리입니다.")
        else if (category.length > 20)
            _categoryState.value = BaseState.Error("카테고리는 20자 이하이어야 합니다.")
        else
            _categoryState.value = BaseState.Success(category.isNotEmpty())
    }

    private fun reorderItem(from: Int, to: Int){
        // 달라진게 있으면 재정렬
        if (from != to){
            _categoryList.add(to, _categoryList.removeAt(from))
            _categoryPostStateList.add(to, _categoryPostStateList.removeAt(from))

            changeSequenceUseCase(_categoryList)
                .catch {
                    _errorChannel.send(UiText.DynamicString(it.message.toString()))
                    _categoryList.add(from, _categoryList.removeAt(to))
                }
                .onEach { isSuccessful ->
                    if (isSuccessful){
                        _categoryList[from].sequence = _categoryList[to].sequence.also {
                            _categoryList[to].sequence = _categoryList[from].sequence
                        }
                    }
                    else{
                        _categoryList.add(from, _categoryList.removeAt(to))
                        _errorChannel.send(UiText.StringResource(R.string.category_swap_error))

                    }
                }.launchIn(viewModelScope)
        }
    }

    // 카테고리 별 세부 전시 조회
    private fun expandCategory(index: Int){
        when (_categoryPostStateList[index]){
            is CategoryPostState.Initial -> {
                _categoryPostStateList[index] = CategoryPostState.Expanded(
                    (_categoryPostStateList[index] as CategoryPostState.Initial).postFlow
                )
            }
            // 단순 확장 Or not
            else ->{
                val postDetail = if (_categoryPostStateList[index] is CategoryPostState.Expanded)
                    (_categoryPostStateList[index] as CategoryPostState.Expanded).postFlow
                else (_categoryPostStateList[index] as CategoryPostState.UnExpanded).postFlow

                _categoryPostStateList[index] = if (_categoryPostStateList[index] is CategoryPostState.Expanded)
                    CategoryPostState.UnExpanded(postDetail)
                else CategoryPostState.Expanded(postDetail)
            }
        }
    }

    private fun pagingLoadError(index: Int){
        viewModelScope.launch {
            _errorChannel.send(UiText.StringResource(R.string.network_error))
//            _categoryPostStateList[index] = CategoryPostState.UnExpanded(
//                (_categoryPostStateList[index] as CategoryPostState.Expanded).postFlow
//            )
        }
    }



    override fun handleEvents(event: CategoryManageEvent) {
        when(event){
            is CategoryManageEvent.CheckEditable -> checkEditable(event.origin, event.edited)
            is CategoryManageEvent.CheckAddable -> checkCategory(event.category)
            is CategoryManageEvent.OnAddClick -> createCategory(event.category)
            is CategoryManageEvent.OnEditClick -> editCategory(event.categoryItem, event.edited)
            is CategoryManageEvent.OnDeleteClick -> deleteCategory(event.categoryItem)
            is CategoryManageEvent.OnExpandClick -> expandCategory(event.index)
            is CategoryManageEvent.OnReorderCategory -> reorderItem(event.from, event.to)
            is CategoryManageEvent.OnLoadError -> pagingLoadError(event.position)
        }
    }
}
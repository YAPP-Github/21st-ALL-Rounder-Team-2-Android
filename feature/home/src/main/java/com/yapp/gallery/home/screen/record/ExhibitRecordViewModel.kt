package com.yapp.gallery.home.screen.record

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.record.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitRecordViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val createRecordUseCase: CreateRecordUseCase,
    private val getTempPostUseCase: GetTempPostUseCase,
    private val updateRecordUseCase: UpdateRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
) : ViewModel(){
    private var _categoryList = mutableStateListOf<CategoryItem>()
    val categoryList : List<CategoryItem>
        get() = _categoryList

    private var _categoryState = MutableStateFlow<BaseState<Boolean>>(BaseState.Loading)
    val categoryState : StateFlow<BaseState<Boolean>>
        get() = _categoryState

    // 전시 기록 화면 상태
    private var _recordScreenState = MutableStateFlow<ExhibitRecordState>(ExhibitRecordState.Initial)
    val recordScreenState : StateFlow<ExhibitRecordState>
        get() = _recordScreenState


    val exhibitName = mutableStateOf("")
    val exhibitDate = mutableStateOf("")
    val exhibitLink = mutableStateOf("")
    val categorySelect = mutableStateOf(-1L)

    init {
        getTempPost()
        getCategoryList()
    }

    // 카테고리 리스트 받기
    private fun getCategoryList(){
        viewModelScope.launch {
            getCategoryListUseCase()
                .catch {  }
                .collect{
                    _categoryList.addAll(it)
                }
        }
    }

    // 임시 저장 확인
    private fun getTempPost(){
        viewModelScope.launch {
            getTempPostUseCase()
                .catch {
                    Log.e("room get", it.message.toString())
                    _recordScreenState.value = ExhibitRecordState.Normal }
                .collectLatest {
                    _recordScreenState.value = ExhibitRecordState.Response(it)
                }
        }
    }

    // 이어서 기록 or 삭제
    fun setContinuousDelete(continuous: Boolean){
        val postInfo = (_recordScreenState.value as ExhibitRecordState.Response).tempPostInfo
        _recordScreenState.value = if (continuous) ExhibitRecordState.Continuous(postInfo).also {
            with(it.tempPostInfo){
                exhibitName.value = name
                exhibitLink.value = postLink ?: ""
                exhibitDate.value = postDate
                categorySelect.value = categoryId
            }
        }else ExhibitRecordState.Delete(postInfo)
    }

    // 삭제 취소
    fun undoDelete(undo: Boolean){
        val postInfo = (_recordScreenState.value as ExhibitRecordState.Delete).tempPostInfo
        if (undo){
            _recordScreenState.value = ExhibitRecordState.Continuous(postInfo).also {
                with(it.tempPostInfo){
                    exhibitName.value = name
                    exhibitLink.value = postLink ?: ""
                    exhibitDate.value = postDate
                    categorySelect.value = categoryId
                }
            }
        }
        else {
            deleteRecord()
            _recordScreenState.value = ExhibitRecordState.Normal
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

    fun createOrUpdateRecord() {
        when (_recordScreenState.value){
            // 일반적 생성 상태
            is ExhibitRecordState.Normal -> {
                viewModelScope.launch {
                    createRecordUseCase(exhibitName.value, categorySelect.value, exhibitDate.value, exhibitLink.value.ifEmpty { null })
                        .catch {
                            Log.e("create error", it.message.toString())
                            _recordScreenState.value = ExhibitRecordState.Normal
                        }
                        .collect{
                            _recordScreenState.value = ExhibitRecordState.Created(it)
                        }
                }
            }
            // 이어서 생성중
            is ExhibitRecordState.Continuous -> {
                val id = (_recordScreenState.value as ExhibitRecordState.Continuous).tempPostInfo.postId
                _recordScreenState.value = ExhibitRecordState.Created(id)
                viewModelScope.launch {
                    updateRecordUseCase(id, exhibitName.value, categorySelect.value, exhibitDate.value, exhibitLink.value.ifEmpty { null })
                        .catch {

                        }
                        .collect{
                            _recordScreenState.value = ExhibitRecordState.Created(it)
                        }
                }
            }
            // 만들어 진적 있음
            is ExhibitRecordState.Created -> {
                val id = (_recordScreenState.value as ExhibitRecordState.Created).postId
                viewModelScope.launch {
                    updateRecordUseCase(id, exhibitName.value, categorySelect.value, exhibitDate.value, exhibitLink.value)
                        .catch {

                        }
                        .collect()
                }
            }
            else -> {}
        }
    }

    private fun deleteRecord(){
        viewModelScope.launch {
            deleteRecordUseCase()
                .catch {
                    Log.e("room failure", it.message.toString())
                }
                .collectLatest {
                    Log.e("room success", "삭제 성공")
                }
        }
    }
}

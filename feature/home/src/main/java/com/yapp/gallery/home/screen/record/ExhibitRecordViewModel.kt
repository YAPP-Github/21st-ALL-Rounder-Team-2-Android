package com.yapp.gallery.home.screen.record

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.usecase.record.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExhibitRecordViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val createRecordUseCase: CreateRecordUseCase,
    private val getTempPostUseCase: GetTempPostUseCase,
    private val deleteTempPostUseCase: DeleteTempPostUseCase
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
        _recordScreenState.value = if (continuous) ExhibitRecordState.Continuous(postInfo)
            else ExhibitRecordState.Delete(postInfo)
    }

    // 삭제 취소
    fun undoDelete(undo: Boolean){
        if (undo){
            val postInfo = (_recordScreenState.value as ExhibitRecordState.Delete).tempPostInfo
            _recordScreenState.value = ExhibitRecordState.Continuous(postInfo)
        }
        else {
            _recordScreenState.value = ExhibitRecordState.Normal
            deleteTempPost()
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

    fun createRecord(name: String, categoryId: Long, postDate: String, link: String?) {
        if (_recordScreenState.value is ExhibitRecordState.Normal){
            _recordScreenState.value = ExhibitRecordState.Crated
            viewModelScope.launch {
                createRecordUseCase(name, categoryId, changeDateFormat(postDate))
                    .catch {
                        Log.e("create error", it.message.toString())
                        _recordScreenState.value = ExhibitRecordState.Normal
                    }
                    .collect{

                    }
            }
        }
    }

    private fun deleteTempPost(){
        viewModelScope.launch {
            deleteTempPostUseCase()
                .catch {
                    Log.e("room failure", it.message.toString())
                }
                .collectLatest {
                    Log.e("room success", "삭제 성공")
                }
        }
    }

    private fun changeDateFormat(postDate: String): String {
        var dateList = postDate.split('/')
        return String.format(
            "%4d-%02d-%02d",
            dateList[0].toInt(),
            dateList[1].toInt(),
            dateList[2].toInt()
        )
    }
}


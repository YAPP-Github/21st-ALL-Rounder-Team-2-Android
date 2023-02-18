package com.yapp.gallery.profile.screen.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.common.model.UiText
import com.yapp.gallery.domain.entity.notice.NoticeItem
import com.yapp.gallery.domain.usecase.notice.GetNoticeListUseCase
import com.yapp.gallery.profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val getNoticeListUseCase: GetNoticeListUseCase
) : ViewModel() {
    private var _noticeListState = MutableStateFlow<BaseState<List<NoticeItem>>>(BaseState.Loading)
    val noticeListState : StateFlow<BaseState<List<NoticeItem>>>
        get() = _noticeListState

    private var _errorChannel = Channel<UiText>()
    val errors = _errorChannel.receiveAsFlow()

    init {
        getNoticeList()
    }

    private fun getNoticeList(){
        viewModelScope.launch {
            getNoticeListUseCase()
                .catch {
                    _noticeListState.value = BaseState.Error(it.message)
                    _errorChannel.send(UiText.StringResource(R.string.notice_get_error))
                }
                .collectLatest {
                    if (it.isNotEmpty())
                        _noticeListState.value = BaseState.Success(it)
                    else
                        _noticeListState.value = BaseState.None
                }
        }
    }
}
package com.yapp.gallery.home.screen.calendar

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.util.webview.NavigatePayload
import com.yapp.gallery.common.util.webview.WebViewState
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getRefreshedTokenUseCase: GetRefreshedTokenUseCase,
) : ViewModel() {
    private val _calendarSideEffect = Channel<NavigatePayload>()
    val calendarSideEffect = _calendarSideEffect.receiveAsFlow()

    private val _calendarState = MutableStateFlow<WebViewState>(WebViewState.Initial)
    val calendarState : StateFlow<WebViewState>
        get() = _calendarState

    init {
        getRefreshedToken()
    }

    fun getRefreshedToken(){
        getRefreshedTokenUseCase()
            .catch {
                Log.e("error", it.message.toString())
                _calendarState.value = WebViewState.Disconnected
            }
            .onEach {
                Log.e("success", it)
                _calendarState.value = WebViewState.Connected(it)
            }
            .launchIn(viewModelScope)
    }

    fun setSideEffect(action: String, payload : String?){
        viewModelScope.launch {
            _calendarSideEffect.send(NavigatePayload(action, payload = null))
        }
        Log.e("homeSideEffect", action)
    }
}
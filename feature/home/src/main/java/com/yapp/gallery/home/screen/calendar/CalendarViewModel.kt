package com.yapp.gallery.home.screen.calendar

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.common.util.webview.NavigatePayload
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    getRefreshedTokenUseCase: GetRefreshedTokenUseCase,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _calendarSideEffect = Channel<NavigatePayload>()
    val calendarSideEffect = _calendarSideEffect.receiveAsFlow()

    private val _idToken = MutableStateFlow<String?>(null)
    val idToken : StateFlow<String?>
        get() = _idToken

    init {
        viewModelScope.launch {
            getRefreshedTokenUseCase()
                .collect{
                    _idToken.value = it
                    it?.let {
                        sharedPreferences.edit().putString("idToken", it).apply()
                    }
                }
        }
    }

    fun setSideEffect(action: String, payload : String?){
        viewModelScope.launch {
            _calendarSideEffect.send(NavigatePayload(action, payload = null))
        }
        Log.e("homeSideEffect", action)
    }
}
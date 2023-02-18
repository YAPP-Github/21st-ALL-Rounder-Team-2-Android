package com.yapp.gallery.home.screen.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.gallery.domain.usecase.auth.GetRefreshedTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getRefreshedTokenUseCase: GetRefreshedTokenUseCase,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _homeSideEffect = Channel<String>()
    val homeSideEffect = _homeSideEffect.receiveAsFlow()

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


    fun setSideEffect(action: String){
        viewModelScope.launch {
            _homeSideEffect.send(action)
        }
        Log.e("homeSideEffect", action)
    }
}
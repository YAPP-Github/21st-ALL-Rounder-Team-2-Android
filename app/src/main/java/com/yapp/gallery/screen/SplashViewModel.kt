package com.yapp.gallery.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel(){
    private val _splashSideEffect = Channel<SplashEffect>()
    val splashSideEffect = _splashSideEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            auth.currentUser?.let {
                _splashSideEffect.send(SplashEffect.MoveToHome)
            } ?: run {
                _splashSideEffect.send(SplashEffect.MoveToLogin)
            }
        }
    }
}
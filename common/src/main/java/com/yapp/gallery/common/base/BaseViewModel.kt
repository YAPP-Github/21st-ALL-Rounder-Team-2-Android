package com.yapp.gallery.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<E : ViewModelContract.Event, SE : ViewModelContract.SideEffect> :
    ViewModel()
{
    private val _sideEffect : Channel<SE> = Channel()
    val sideEffect = _sideEffect.receiveAsFlow()

    protected fun sendSideEffect(effect: SE){
        viewModelScope.launch{
            _sideEffect.send(effect)
        }
    }

    fun setEvent(event: E){
        handleEvents(event)
    }

    abstract fun handleEvents(event: E)
}
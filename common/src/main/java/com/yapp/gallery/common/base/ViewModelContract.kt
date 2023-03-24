package com.yapp.gallery.common.base

sealed interface ViewModelContract{
    interface State: ViewModelContract
    // ViewModel 에서 처리
    interface Event: ViewModelContract
    // View 에서 처리
    interface SideEffect: ViewModelContract
}
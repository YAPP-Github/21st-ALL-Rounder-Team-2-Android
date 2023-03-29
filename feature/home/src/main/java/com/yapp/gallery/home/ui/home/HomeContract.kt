package com.yapp.gallery.home.ui.home

import com.yapp.gallery.common.base.ViewModelContract

class HomeContract {
    sealed class HomeState : ViewModelContract.State{
        object Initial : HomeState()
        data class Connected(val idToken : String) : HomeState()
        object Disconnected : HomeState()
    }

    sealed class HomeEvent : ViewModelContract.Event{
        object OnLoadAgain : HomeEvent()
        data class OnWebViewClick (val action: String, val payload: String?) : HomeEvent()
    }

    sealed class HomeSideEffect : ViewModelContract.SideEffect{
        object NavigateToCalendar : HomeSideEffect()
        object NavigateToRecord : HomeSideEffect()
        object NavigateToProfile : HomeSideEffect()
        data class NavigateToInfo(val postId: Long) : HomeSideEffect()
    }
}
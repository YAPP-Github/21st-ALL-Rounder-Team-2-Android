package com.yapp.gallery.profile.ui.profile

import com.yapp.gallery.common.base.ViewModelContract
import com.yapp.gallery.domain.entity.profile.User

class ProfileContract {
    sealed class ProfileState : ViewModelContract.State{
        object Initial : ProfileState()
        data class Success(val user: User) : ProfileState()
        data class Error(val message: String?) : ProfileState()
    }

    sealed class ProfileEvent : ViewModelContract.Event{
        object OnNicknameClick : ProfileEvent()
        object OnNoticeClick : ProfileEvent()
        object OnLegacyClick : ProfileEvent()
        object OnSignOutClick : ProfileEvent()
        object OnManageClick : ProfileEvent()
        object OnLogout : ProfileEvent()
    }

    sealed class ProfileSideEffect : ViewModelContract.SideEffect{
        object NavigateToLogin : ProfileSideEffect()
        object NavigateToManage: ProfileSideEffect()
        object NavigateToNickname: ProfileSideEffect()
        object NavigateToNotice: ProfileSideEffect()
        object NavigateToLegacy: ProfileSideEffect()
        object NavigateToSignOut: ProfileSideEffect()
    }
}
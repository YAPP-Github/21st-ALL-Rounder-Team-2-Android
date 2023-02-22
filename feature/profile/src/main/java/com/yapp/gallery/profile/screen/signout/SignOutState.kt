package com.yapp.gallery.profile.screen.signout

import com.yapp.gallery.common.model.UiText

sealed class SignOutState{
    object Initial : SignOutState()
    object Success : SignOutState()
    data class Failure(val message: UiText) : SignOutState()
}

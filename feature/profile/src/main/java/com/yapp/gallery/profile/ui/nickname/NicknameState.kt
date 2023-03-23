package com.yapp.gallery.profile.ui.nickname

sealed class NicknameState{
    object None : NicknameState()
    data class Normal(val nickname: String): NicknameState()
    data class Error(val message: String): NicknameState()
}

package com.yapp.gallery.screen

sealed class SplashEffect{
    object MoveToHome : SplashEffect()
    object MoveToLogin : SplashEffect()
}

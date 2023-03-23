package com.yapp.gallery.ui

sealed class SplashEffect{
    object MoveToHome : SplashEffect()
    object MoveToLogin : SplashEffect()
}
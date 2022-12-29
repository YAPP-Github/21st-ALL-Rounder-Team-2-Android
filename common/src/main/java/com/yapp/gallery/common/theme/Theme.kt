package com.yapp.gallery.common.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun GalleryTheme(
    content: @Composable() () -> Unit
){
    // Status bar color 고정
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = color_popUpBottom,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = color_background,
            darkIcons = false
        )
    }

    MaterialTheme(
        content = content,
        colors = colorScheme,
        typography = typography
    )
}
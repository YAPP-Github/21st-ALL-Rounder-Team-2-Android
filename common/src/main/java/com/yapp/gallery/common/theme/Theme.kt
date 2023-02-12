package com.yapp.gallery.common.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun GalleryTheme(
    content: @Composable() () -> Unit
){
    MaterialTheme(
        content = content,
        colors = colorScheme,
        typography = typography
    )
}
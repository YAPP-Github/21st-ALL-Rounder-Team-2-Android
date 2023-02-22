package com.yapp.gallery.common.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val grey_838383 = Color(0xFF838383)
val grey_bdbdbd = Color(0xFFbdbdbd)
val grey_929191 = Color(0xFF929191)

val black_4f4f4f = Color(0xFF4f4f4f)
val black_252525 = Color(0xFF252525)

// 검정, 화이트 색상
val color_white = Color(0xFFFFFFFF)
val color_black = Color(0xFF000000)
val color_background = Color(0xFF161616)
val color_popUpBottom = Color(0xFF1F1F1F)

// 블루, 그린 색상
val color_mainBlue = Color(0xFFBCD4FE)
val color_mainGreen = Color(0xFFE1FCAD)

// 그레이 색상
val color_gray900 = Color(0xFF3F3F3F)
val color_gray800 = Color(0xFF555555)
val color_gray700 = Color(0xFF727272)
val color_gray600 = Color(0xFF979797)
val color_gray500 = Color(0xFFB4B4B4)
val color_gray400 = Color(0xFFD9D9D9)
val color_gray300 = Color(0xFFEAEAEA)

val color_purple500 = Color(0xFF6200EE)

// Color Scheme 지정
val colorScheme = Colors(
    primary = color_mainGreen,
    primaryVariant = color_mainGreen,
    secondary = color_mainBlue,
    secondaryVariant = color_mainBlue,
    background = color_background,
    error = Color.Red,
    onError = color_white,
    onPrimary = color_white,
    onSurface = color_white,
    onSecondary = color_white,
    onBackground = color_white,
    surface = color_popUpBottom,
    isLight = false
)
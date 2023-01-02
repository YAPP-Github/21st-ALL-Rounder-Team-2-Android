package com.yapp.gallery.common.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yapp.gallery.common.R


val pretendard = FontFamily(
    Font(R.font.pretendard_regular),
    Font(R.font.pretendard_medium, weight = FontWeight.Medium),
    Font(R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
    Font(R.font.pretendard_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.pretendard_light, weight = FontWeight.Light)
)

val typography = Typography(
    defaultFontFamily = pretendard,
    // 적용 예시(H1/18/SemiBold) style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.SemiBold)

    // H1/24
    h1 = TextStyle(
        fontSize = 24.sp,
        color = color_white
    ),
    // H2/18,
    h2 = TextStyle(
        fontSize = 18.sp,
        color = color_white
    ),
    // H3/16
    h3 = TextStyle(
        fontSize = 16.sp,
        color = color_white
    ),
    // H4/14
    h4 = TextStyle(
        fontSize = 14.sp,
        color = color_white
    ),
    // H5/12
    h5 = TextStyle(
        fontSize = 12.sp,
        color = color_white
    ),

)
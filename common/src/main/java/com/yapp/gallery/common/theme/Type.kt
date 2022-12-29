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
    // Todo : 피그마에 H0부터 정의 되어있지만 Typography에는 h1부터 있어서 한 순서 뒤로 밀려서 적용함!
    // 적용 예시(H1/18/SemiBold) style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.SemiBold)

    // H0/24
    h1 = TextStyle(
        fontSize = 24.sp,
        color = color_white
    ),
    // H1/18,
    h2 = TextStyle(
        fontSize = 18.sp,
        color = color_white
    ),
    // H2/16
    h3 = TextStyle(
        fontSize = 16.sp,
        color = color_white
    ),
    // H3/14
    h4 = TextStyle(
        fontSize = 14.sp,
        color = color_white
    ),
    // H4/12
    h5 = TextStyle(
        fontSize = 12.sp,
        color = color_white
    ),

)
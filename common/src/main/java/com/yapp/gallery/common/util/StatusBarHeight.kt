package com.yapp.gallery.common.util

import android.app.Activity
import kotlin.math.roundToInt

fun getStatusBarHeight(context: Activity) : Int{
    val deviceDensity = context.resources.displayMetrics.density

    val idStatusBarHeight: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusHeight : Float =
        if (idStatusBarHeight > 0) context.resources.getDimensionPixelSize(idStatusBarHeight) / deviceDensity else 0f
    return statusHeight.roundToInt()
}
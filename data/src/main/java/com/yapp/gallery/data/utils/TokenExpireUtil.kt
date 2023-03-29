package com.yapp.gallery.data.utils

import android.util.Log


internal fun getTokenExpiredTime() : String {
    // 한시간 뒤 밀리 초
    return (System.currentTimeMillis() + 3500000).toString()
}

internal fun isTokenExpired(comparedTime: String) : Boolean{
    val currentTime = System.currentTimeMillis()
    val expiredTime = comparedTime.toLong()
    Log.e("expiredTime", expiredTime.toString())
    Log.e("currentTime", currentTime.toString())
    return currentTime > expiredTime
}
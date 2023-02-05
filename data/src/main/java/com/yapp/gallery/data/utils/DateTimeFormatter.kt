package com.yapp.gallery.data.utils

fun changeDateFormat(postDate: String): String {
    var dateList = postDate.split('/')
    return String.format(
        "%4d-%02d-%02d",
        dateList[0].toInt(),
        dateList[1].toInt(),
        dateList[2].toInt()
    )
}
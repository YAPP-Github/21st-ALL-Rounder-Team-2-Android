package com.yapp.gallery.domain.entity.home

data class CategoryItem(
    val id: Long,
    val name: String,
    var sequence: Int,
    val postNum: Int
)

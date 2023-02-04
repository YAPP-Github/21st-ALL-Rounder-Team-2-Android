package com.yapp.gallery.domain.entity.home

data class TempPostInfo(
    val postId: Long,
    val name: String,
    val categoryId: Long,
    val postDate: String,
    val postLink: String?
)

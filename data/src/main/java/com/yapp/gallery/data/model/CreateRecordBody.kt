package com.yapp.gallery.data.model

data class CreateRecordBody(
    val name: String,
    val categoryId: Long,
    val postDate: String
)

package com.yapp.gallery.data.model

data class UserResponse(
    val createdAt: String,
    val id: Long,
    val name: String,
    val profileImage: String?,
    val uid: String,
    val updatedAt: String
)
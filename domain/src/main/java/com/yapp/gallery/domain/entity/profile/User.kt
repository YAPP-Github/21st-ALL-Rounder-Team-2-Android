package com.yapp.gallery.domain.entity.profile

data class User(
    val id : Long,
    val uid : String,
    val name : String,
    val profileImage : String?
)

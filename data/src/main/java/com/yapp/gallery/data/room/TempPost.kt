package com.yapp.gallery.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TempPostTable")
data class TempPost(
    @PrimaryKey val postId: Long,
    val name: String,
    val categoryId: Long,
    val postDate: String,
    val postLink: String?
)

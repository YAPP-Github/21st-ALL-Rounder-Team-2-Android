package com.yapp.gallery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TempPost(
    @PrimaryKey val postId: Long,
)

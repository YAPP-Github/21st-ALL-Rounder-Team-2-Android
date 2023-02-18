package com.yapp.gallery.domain.repository

import kotlinx.coroutines.flow.Flow

interface ExhibitEditRepository{
    fun updateRemotePost(postId: Long, name: String, categoryId: Long, postDate: String, postLink: String?)
        : Flow<Boolean>

    fun deleteRemotePost(postId: Long) : Flow<Boolean>
}
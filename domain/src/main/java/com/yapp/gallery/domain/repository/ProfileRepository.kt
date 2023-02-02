package com.yapp.gallery.domain.repository

import com.yapp.gallery.domain.entity.profile.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun loadUserData() : Flow<User>
    fun editCategory(categoryId: Long, editedName: String) : Flow<String>
}
package com.yapp.gallery.data.source.remote.profile

import com.yapp.gallery.domain.entity.profile.User
import kotlinx.coroutines.flow.Flow

interface ProfileRemoteDataSource {
    fun loadUserData() : Flow<User>
    fun changeNickname(userId: Long, editedName: String) : Flow<Boolean>
    fun signOut() : Flow<Boolean>
}
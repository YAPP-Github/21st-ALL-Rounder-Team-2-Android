package com.yapp.gallery.data.remote.profile

import com.yapp.gallery.data.model.UserResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRemoteDataSource {
    fun loadUserData() : Flow<UserResponse>
}
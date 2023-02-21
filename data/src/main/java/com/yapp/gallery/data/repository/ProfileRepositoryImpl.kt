package com.yapp.gallery.data.repository

import com.yapp.gallery.data.source.remote.profile.ProfileRemoteDataSource
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override fun loadUserData(): Flow<User> {
        return profileRemoteDataSource.loadUserData()
    }

    override fun changeNickname(userId: Long, editedName: String): Flow<Boolean> {
        return profileRemoteDataSource.changeNickname(userId, editedName)
    }

    override fun signOut(): Flow<Boolean> {
        return profileRemoteDataSource.signOut()
    }
}
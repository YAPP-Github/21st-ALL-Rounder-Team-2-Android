package com.yapp.gallery.data.repository

import android.provider.ContactsContract.Profile
import com.yapp.gallery.data.remote.profile.ProfileRemoteDataSource
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override fun loadUserData(): Flow<User> {
        return profileRemoteDataSource.loadUserData().map {
            User(it.id ,it.uid, it.name, it.profileImage)
        }
    }
}
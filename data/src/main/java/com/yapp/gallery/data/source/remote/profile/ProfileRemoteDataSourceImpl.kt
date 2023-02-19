package com.yapp.gallery.data.source.remote.profile

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.domain.entity.profile.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val artieSerivce: ArtieSerivce,
    @IoDispatcher private val dispatcher : CoroutineDispatcher
) : ProfileRemoteDataSource {
    override fun loadUserData(): Flow<User> = flow {
        emit(artieSerivce.getUserData())
    }.flowOn(dispatcher)

    override fun changeNickname(
        userId: Long, editedName: String
    ): Flow<Boolean> = flow {
        emit(artieSerivce.updateNickname(userId, editedName).isSuccessful)
    }.flowOn(dispatcher)

    override fun signOut(): Flow<Boolean> = flow {
        emit(artieSerivce.signOut().isSuccessful)
    }.flowOn(dispatcher)
}
package com.yapp.gallery.data.repository

import com.yapp.gallery.data.source.remote.login.LoginRemoteDataSource
import com.yapp.gallery.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository{
    override fun kakaoLogin(accessToken: String): Flow<String> {
        return loginRemoteDataSource.kakaoLogin(accessToken).map { it.firebase_token }
    }

    override fun naverLogin(accessToken: String): Flow<String> {
        return loginRemoteDataSource.naverLogin(accessToken).map { it.firebase_token }
    }


    override fun createUser(firebaseUserId: String): Flow<Long>{
        return loginRemoteDataSource.createUser(firebaseUserId).map { it.id }
    }
}
package com.yapp.gallery.data.repository

import com.yapp.gallery.data.remote.login.LoginRemoteDataSource
import com.yapp.gallery.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource
) : LoginRepository{
    override suspend fun kakaoLogin(accessToken: String): String {
        return loginRemoteDataSource.kakaoLogin(accessToken).firebase_token
    }
}
package com.yapp.gallery.data.source.remote.login

import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken

interface LoginRemoteDataSource {
    suspend fun kakaoLogin(accessToken: String) : FirebaseToken
    suspend fun naverLogin(accessToken: String) : FirebaseToken
    suspend fun createUser(firebaseUserId: String) : CreateUserResponse
}
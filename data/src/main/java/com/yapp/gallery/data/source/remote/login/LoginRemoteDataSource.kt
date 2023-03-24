package com.yapp.gallery.data.source.remote.login

import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken
import kotlinx.coroutines.flow.Flow

interface LoginRemoteDataSource {
    fun kakaoLogin(accessToken: String) : Flow<FirebaseToken>
    fun naverLogin(accessToken: String) : Flow<FirebaseToken>
    fun createUser(firebaseUserId: String) : Flow<CreateUserResponse>
}
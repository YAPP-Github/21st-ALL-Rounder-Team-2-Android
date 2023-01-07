package com.yapp.gallery.data.remote.login

import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken

interface LoginRemoteDataSource {
    suspend fun tokenLogin(accessToken: String) : FirebaseToken
    suspend fun createUser(idToken: String, firebaseUserId: String) : CreateUserResponse
}
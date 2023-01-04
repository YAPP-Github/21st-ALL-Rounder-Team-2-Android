package com.yapp.gallery.data.remote.login

import com.yapp.gallery.domain.entity.login.FirebaseToken

interface LoginRemoteDataSource {
    suspend fun kakaoLogin(accessToken: String) : FirebaseToken
}
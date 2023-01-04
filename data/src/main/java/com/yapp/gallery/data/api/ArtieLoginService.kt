package com.yapp.gallery.data.api

import com.yapp.gallery.domain.entity.login.FirebaseToken
import retrofit2.http.POST

interface ArtieLoginService {
    // 카카오 로그인
    @POST("/verifyToken")
    suspend fun kakaoLogin(accessToken : String) : FirebaseToken
}
package com.yapp.gallery.data.api

import com.yapp.gallery.data.body.TokenLoginBody
import com.yapp.gallery.domain.entity.login.FirebaseToken
import retrofit2.http.Body
import retrofit2.http.POST

interface ArtieTokenService {
    // 토큰 로그인
    @POST("/verifyToken")
    suspend fun tokenLogin(@Body tokenLoginBody: TokenLoginBody) : FirebaseToken
}
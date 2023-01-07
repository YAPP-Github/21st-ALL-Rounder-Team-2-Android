package com.yapp.gallery.data.api

import com.yapp.gallery.domain.entity.login.CreateUserResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ArtieLoginSerivce {
    @POST("/user")
    suspend fun createUser(
        @Header("Authorization") idToken: String,
        @Query("uid") userId: String
    ) : CreateUserResponse
}
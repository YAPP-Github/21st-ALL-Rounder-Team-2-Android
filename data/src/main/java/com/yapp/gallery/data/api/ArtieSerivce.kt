package com.yapp.gallery.data.api

import com.yapp.gallery.domain.entity.login.CreateUserResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ArtieSerivce {
    @POST("/user")
    suspend fun createUser(@Query("uid") userId: String) : CreateUserResponse
}
package com.yapp.gallery.data.api

import com.yapp.gallery.domain.entity.login.CreateUserResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ArtieService {
    @POST("/user")
    suspend fun postUser(@Query("uid") userId: String) : CreateUserResponse
}
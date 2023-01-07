package com.yapp.gallery.data.api

import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ArtieSerivce {
    // 유저 회원 가입
    @POST("/user")
    suspend fun createUser(@Query("uid") userId: String) : CreateUserResponse

    // 카테고리 조회
    @GET("/category")
    suspend fun getCategoryList() : List<CategoryItem>
}
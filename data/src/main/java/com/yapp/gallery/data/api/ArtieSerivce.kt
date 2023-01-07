package com.yapp.gallery.data.api

import com.yapp.gallery.data.body.CategoryCreateBody
import com.yapp.gallery.domain.entity.home.CategoryCreated
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import retrofit2.http.*

interface ArtieSerivce {
    // 유저 회원 가입
    @POST("/user")
    suspend fun createUser(@Query("uid") userId: String) : CreateUserResponse

    // 카테고리 조회
    @GET("/category")
    suspend fun getCategoryList() : List<CategoryItem>

    // 카테고리 생성
    @POST("/category")
    suspend fun createCategory(@Body categoryCreateBody: CategoryCreateBody) : CategoryCreated
}
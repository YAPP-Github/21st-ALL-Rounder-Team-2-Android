package com.yapp.gallery.data.api

import com.yapp.gallery.data.model.CategoryCreateBody
import com.yapp.gallery.data.model.CreateRecordBody
import com.yapp.gallery.data.model.UserResponse
import com.yapp.gallery.domain.entity.home.CreatedId
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
    suspend fun createCategory(@Body categoryCreateBody: CategoryCreateBody) : CreatedId

    // 전시 생성
    @POST("/post")
    suspend fun createRecord(@Body createRecordBody: CreateRecordBody) : CreatedId

    // 유저 조회
    @GET("/user/me")
    suspend fun getUserData() : UserResponse
}
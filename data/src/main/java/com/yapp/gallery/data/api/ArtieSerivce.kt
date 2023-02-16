package com.yapp.gallery.data.api

import com.yapp.gallery.data.model.CategoryBody
import com.yapp.gallery.data.model.CreateRecordBody
import com.yapp.gallery.domain.entity.category.CategoryPost
import com.yapp.gallery.domain.entity.home.CreatedId
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.notice.NoticeItem
import com.yapp.gallery.domain.entity.profile.User
import retrofit2.Response
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
    suspend fun createCategory(@Body categoryBody: CategoryBody) : CreatedId

    // 카테고리 편집
    @PUT("/category/{id}")
    suspend fun editCategory(@Path("id") categoryId: Long, @Body categoryBody: CategoryBody) : Response<Unit>

    // 카테고리 삭제
    @DELETE("/category/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Long) : Response<Unit>

    // 카테고리 순서 변경
    @PUT("/category/sequence")
    suspend fun changeCategorySequence(@Body categoryList : List<CategoryItem>) : Response<Unit>

    // 전시 생성
    @POST("/post")
    suspend fun createRecord(@Body createRecordBody: CreateRecordBody) : CreatedId

    // 전시 업데이트
    @PUT("/post/{id}")
    suspend fun updateRecord(@Path("id") postId: Long, @Body createRecordBody: CreateRecordBody) : Response<Unit>

    // 전시 삭제
    @DELETE("/post/{id}")
    suspend fun deleteRecord(@Path("id") postId: Long) : Response<Unit>

    // 유저 조회
    @GET("/user/my-page")
    suspend fun getUserData() : User

    // 카테고리 별 전시 목록 조회
    @GET("/post/category/{id}")
    suspend fun getCategoryPost(@Path("id") id: Long, @Query("page") page: Int, @Query("size") size: Int) : CategoryPost

    // 공지사항 조회
    @GET("/notice")
    suspend fun getNoticeList() : List<NoticeItem>
}
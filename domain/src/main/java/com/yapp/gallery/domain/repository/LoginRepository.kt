package com.yapp.gallery.domain.repository


interface LoginRepository {
    suspend fun kakaoLogin(accessToken : String) : String
    suspend fun naverLogin(accessToken: String) : String
    suspend fun createUser(firebaseUserId: String) : Long
}
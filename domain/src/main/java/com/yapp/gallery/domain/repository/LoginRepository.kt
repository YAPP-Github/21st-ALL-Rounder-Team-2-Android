package com.yapp.gallery.domain.repository

import kotlinx.coroutines.flow.Flow


interface LoginRepository {
    fun kakaoLogin(accessToken : String) : Flow<String>
    fun naverLogin(accessToken: String) : Flow<String>
    fun createUser(firebaseUserId: String) : Flow<Long>
}
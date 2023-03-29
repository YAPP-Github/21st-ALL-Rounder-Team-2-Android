package com.yapp.gallery.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun setLoginType(loginType: String) : Flow<Unit>
    fun setIdToken(idToken: String) : Flow<Unit>
    suspend fun getLoginType() : String?
    suspend fun getIdToken() : String
    suspend fun getRefreshedToken() : String

    fun deleteLoginInfo() : Flow<Unit>

    fun getValidToken() : Flow<String>
}
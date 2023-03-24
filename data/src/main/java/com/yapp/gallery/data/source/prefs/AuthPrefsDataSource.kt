package com.yapp.gallery.data.source.prefs

import kotlinx.coroutines.flow.Flow

interface AuthPrefsDataSource {
    suspend fun setLoginType(loginType: String)
    suspend fun setIdToken(idToken: String)
    fun getRefreshedToken() : Flow<String>
    suspend fun getIdToken() : String?
}
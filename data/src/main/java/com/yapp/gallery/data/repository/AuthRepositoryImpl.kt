package com.yapp.gallery.data.repository

import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.source.prefs.AuthPrefsDataSource
import com.yapp.gallery.data.utils.isTokenExpired
import com.yapp.gallery.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authPrefsDataSource: AuthPrefsDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthRepository{
    override fun setLoginType(loginType: String): Flow<Unit> = flow {
        emit(authPrefsDataSource.setLoginType(loginType))
    }.flowOn(dispatcher)

    override fun setIdToken(idToken: String): Flow<Unit> = flow {
        emit(authPrefsDataSource.setIdToken(idToken))
    }.flowOn(dispatcher)

    override suspend fun getLoginType(): String? {
        return authPrefsDataSource.getLoginType()
    }

    override suspend fun getIdToken(): String {
        return authPrefsDataSource.getIdToken().firstOrNull() ?: ""
    }

    override suspend fun getRefreshedToken(): String {
        return authPrefsDataSource.getRefreshedToken().also {
            // 리프레시 된 것 새로 저장
            authPrefsDataSource.setIdToken(it)
        }
    }

    override fun deleteLoginInfo(): Flow<Unit> = flow {
        emit(authPrefsDataSource.deleteLoginInfo())
    }.flowOn(dispatcher)

    // 유효한 토큰 가져오기
    // 토큰 만료된 경우 자동으로 리프레시 토큰 가져옴
    override fun getValidToken(): Flow<String> {
        return authPrefsDataSource.getIdTokenExpiredTime().map {
            if (it.isEmpty() || isTokenExpired(it)) {
                getRefreshedToken()
            } else {
                getIdToken()
            }
        }.flowOn(dispatcher)
    }

}
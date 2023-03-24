package com.yapp.gallery.data.repository

import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.source.prefs.AuthPrefsDataSource
import com.yapp.gallery.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authPrefsDataSource: AuthPrefsDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthRepository{
    // Todo : Firebase Token 한 시간 만료인걸 계산 -> 자동 리프레시?
    override fun setLoginType(loginType: String): Flow<Unit> = flow {
        emit(authPrefsDataSource.setLoginType(loginType))
    }.flowOn(dispatcher)

    override fun setIdToken(idToken: String): Flow<Unit> = flow {
        emit(authPrefsDataSource.setIdToken(idToken))
    }.flowOn(dispatcher)

    override fun getLoginType(): Flow<String> {
        return authPrefsDataSource.getLoginType()
    }

    override suspend fun getIdToken(): String {
        return authPrefsDataSource.getIdToken() ?: ""
    }

    override fun getRefreshedToken(): Flow<String> {
        return authPrefsDataSource.getRefreshedToken()
            .onEach {
                authPrefsDataSource.setIdToken(it)
            }
    }

    override fun deleteLoginInfo(): Flow<Unit> = flow {
        emit(authPrefsDataSource.deleteLoginInfo())
    }.flowOn(dispatcher)
}
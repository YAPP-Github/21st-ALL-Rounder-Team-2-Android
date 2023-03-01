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
    override fun setLoginType(loginType: String): Flow<Unit> = flow {
        emit(authPrefsDataSource.setLoginType(loginType))
    }.flowOn(dispatcher)

    override fun setIdToken(idToken: String): Flow<Unit> = flow {
        emit(authPrefsDataSource.setIdToken(idToken))
    }.flowOn(dispatcher)

    override fun getIdToken(): Flow<String> {
        return authPrefsDataSource.getIdToken()
    }

    override fun getRefreshedToken(): Flow<String> {
        return authPrefsDataSource.getRefreshedToken()
            .onEach {
                authPrefsDataSource.setIdToken(it)
            }
    }
}
package com.yapp.gallery.data.source.remote.login

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.api.login.ArtieKakaoService
import com.yapp.gallery.data.api.login.ArtieNaverService
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.model.TokenLoginBody
import com.yapp.gallery.domain.entity.login.CreateUserResponse
import com.yapp.gallery.domain.entity.login.FirebaseToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRemoteDataSourceImpl @Inject constructor(
    private val artieKakaoService: ArtieKakaoService,
    private val artieNaverService: ArtieNaverService,
    private val artieService: ArtieSerivce,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LoginRemoteDataSource {
    override fun kakaoLogin(accessToken: String): Flow<FirebaseToken> = flow {
        emit(artieKakaoService.tokenLogin(TokenLoginBody(accessToken)))
    }.flowOn(ioDispatcher)

    override fun naverLogin(accessToken: String): Flow<FirebaseToken> = flow {
        emit(artieNaverService.tokenLogin(TokenLoginBody(accessToken)))
    }.flowOn(ioDispatcher)
    override fun createUser(firebaseUserId: String): Flow<CreateUserResponse> = flow {
        emit(artieService.createUser(firebaseUserId))
    }.flowOn(ioDispatcher)
}
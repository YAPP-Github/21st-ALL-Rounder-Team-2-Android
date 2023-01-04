package com.yapp.gallery.domain.usecase.login

import com.yapp.gallery.domain.repository.LoginRepository
import javax.inject.Inject

class PostTokenLoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(accessToken : String) = repository.kakaoLogin(accessToken)
}
package com.yapp.gallery.domain.usecase.auth

import com.yapp.gallery.domain.repository.AuthRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import javax.inject.Inject

@OptIn(FlowPreview::class)
class SetLoginInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(loginType: String, idToken: String) = authRepository.setLoginType(loginType).flatMapConcat {
        authRepository.setIdToken(idToken)
    }
}
package com.yapp.gallery.domain.usecase.login

import com.yapp.gallery.domain.repository.LoginRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(firebaseUserId: String) = repository.createUser(firebaseUserId)
}
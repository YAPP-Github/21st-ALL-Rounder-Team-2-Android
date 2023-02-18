package com.yapp.gallery.data.repository

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val auth: FirebaseAuth
) : AuthRepository{
    override fun getRefreshedToken(): Flow<String?> = flow {
        val task = auth.currentUser!!.getIdToken(true)
        emit(Tasks.await(task).token.also {
            Log.e("token refresh", it.toString())
        })
    }.flowOn(dispatcher)
}
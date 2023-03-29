package com.yapp.gallery.data.source.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.data.di.DataStoreModule
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.utils.getTokenExpiredTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthPrefsDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthPrefsDataSource {

    override suspend fun setLoginType(loginType: String) {
        withContext(dispatcher){
            dataStore.edit { preferences ->
                preferences[DataStoreModule.loginTypeKey] = loginType
            }
        }
    }

    override suspend fun setIdToken(idToken: String) {
        withContext(dispatcher){
            dataStore.edit { preferences ->
                preferences[DataStoreModule.idTokenKey] = idToken
                preferences[DataStoreModule.idTokenExpireKey] = getTokenExpiredTime()
            }
        }
    }

    override suspend fun getLoginType(): String? =
        dataStore.data.map { preferences ->
            preferences[DataStoreModule.loginTypeKey] ?: ""
        }.flowOn(dispatcher).firstOrNull()


    override suspend fun getRefreshedToken() : String =
        suspendCancellableCoroutine {continuation ->
            auth.currentUser?.let {user ->
                user.getIdToken(true).addOnSuccessListener {
                    continuation.resume(it.token ?: return@addOnSuccessListener)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            } ?: run { continuation.resumeWithException(Throwable("user is null")) }
        }
//    override fun getRefreshedToken(): Flow<String> = callbackFlow {
//        auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
//            result.token?.let {
//                trySend(it)
//            }
//        }?.addOnFailureListener {
//            close(Error(it))
//        }
//
//        awaitClose()
//    }.flowOn(dispatcher)

    override fun getIdToken(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[DataStoreModule.idTokenKey] ?: ""
        }.flowOn(dispatcher)

    override fun getIdTokenExpiredTime(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[DataStoreModule.idTokenExpireKey] ?: ""
        }.flowOn(dispatcher)

    override suspend fun deleteLoginInfo() {
        dataStore.edit { preferences ->
            preferences.remove(DataStoreModule.loginTypeKey)
            preferences.remove(DataStoreModule.idTokenKey)
        }
    }
}
package com.yapp.gallery.data.source.prefs

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import com.yapp.gallery.data.di.DataStoreModule
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthPrefsDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthPrefsDataSource {

    override suspend fun setLoginType(loginType: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreModule.loginTypeKey] = loginType
        }
    }

    override suspend fun setIdToken(idToken: String) {
        Log.e("idToken", idToken)
        dataStore.edit {preferences ->
            preferences[DataStoreModule.idTokenKey] = idToken
        }
    }

    override fun getLoginType(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[DataStoreModule.loginTypeKey] ?: ""
        }.flowOn(dispatcher)


    override fun getRefreshedToken(): Flow<String> = callbackFlow {
        auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
            result.token?.let {
                trySend(it)
            }
        }?.addOnFailureListener {
            close(Error(it))
        }

        awaitClose()
    }.flowOn(dispatcher)

    override suspend fun getIdToken(): String? =
        dataStore.data.map { preferences ->
            preferences[DataStoreModule.idTokenKey] ?: ""
        }.flowOn(dispatcher).firstOrNull()

    override suspend fun deleteLoginInfo() {
        dataStore.edit { preferences ->
            preferences.remove(DataStoreModule.loginTypeKey)
            preferences.remove(DataStoreModule.idTokenKey)
        }
    }
}
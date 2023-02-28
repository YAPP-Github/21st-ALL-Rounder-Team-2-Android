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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    override fun getRefreshedToken(): Flow<String?> = callbackFlow {
        auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
            trySend(result.token)
        }

        awaitClose { }
    }.flowOn(dispatcher)

    override fun getIdToken(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[DataStoreModule.idTokenKey] ?: ""
        }.flowOn(dispatcher)
}
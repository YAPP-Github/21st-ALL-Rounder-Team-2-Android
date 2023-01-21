package com.yapp.gallery.login.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.yapp.gallery.login.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LoginModule {
    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context, gso : GoogleSignInOptions) : GoogleSignInClient {
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    fun provideGoogleSignInOptions() : GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoClient() : UserApiClient = UserApiClient.instance
}
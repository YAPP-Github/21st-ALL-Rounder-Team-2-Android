package com.yapp.gallery.data.di

import com.yapp.gallery.data.remote.login.LoginRemoteDataSource
import com.yapp.gallery.data.remote.login.LoginRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLoginRemoteDataSource(
        dataSource: LoginRemoteDataSourceImpl
    ): LoginRemoteDataSource
}
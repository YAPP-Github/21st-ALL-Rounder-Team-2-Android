package com.yapp.gallery.data.di

import com.yapp.gallery.data.remote.login.LoginRemoteDataSource
import com.yapp.gallery.data.remote.login.LoginRemoteDataSourceImpl
import com.yapp.gallery.data.remote.profile.ProfileRemoteDataSource
import com.yapp.gallery.data.remote.profile.ProfileRemoteDataSourceImpl
import com.yapp.gallery.data.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.data.remote.record.ExhibitRecordRemoteDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindExhibitRecordRemoteDataSource(
        dataSource: ExhibitRecordRemoteDataSourceImpl
    ) : ExhibitRecordRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        dataSource: ProfileRemoteDataSourceImpl
    ) : ProfileRemoteDataSource
}
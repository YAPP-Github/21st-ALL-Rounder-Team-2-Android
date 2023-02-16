package com.yapp.gallery.data.di

import com.yapp.gallery.data.source.local.record.ExhibitRecordLocalDataSource
import com.yapp.gallery.data.source.local.record.ExhibitRecordLocalDataSourceImpl
import com.yapp.gallery.data.source.remote.category.CategoryManageRemoteDataSource
import com.yapp.gallery.data.source.remote.category.CategoryManageRemoteDataSourceImpl
import com.yapp.gallery.data.source.remote.login.LoginRemoteDataSource
import com.yapp.gallery.data.source.remote.login.LoginRemoteDataSourceImpl
import com.yapp.gallery.data.source.remote.notice.NoticeRemoteDataSource
import com.yapp.gallery.data.source.remote.notice.NoticeRemoteDataSourceImpl
import com.yapp.gallery.data.source.remote.profile.ProfileRemoteDataSource
import com.yapp.gallery.data.source.remote.profile.ProfileRemoteDataSourceImpl
import com.yapp.gallery.data.source.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.data.source.remote.record.ExhibitRecordRemoteDataSourceImpl
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
    abstract fun bindExhibitRecordLocalDataSource(
        dataSource: ExhibitRecordLocalDataSourceImpl
    ) : ExhibitRecordLocalDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        dataSource: ProfileRemoteDataSourceImpl
    ) : ProfileRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCategoryManageRemoteDataSource(
        dataSource: CategoryManageRemoteDataSourceImpl
    ) : CategoryManageRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNoticeRemoteDataSource(
        dataSource: NoticeRemoteDataSourceImpl
    ) : NoticeRemoteDataSource
}
package com.yapp.gallery.data.di

import com.yapp.gallery.data.repository.*
import com.yapp.gallery.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLoginRepository(repositoryImpl: LoginRepositoryImpl) : LoginRepository

    @Binds
    @Singleton
    abstract fun bindExhibitRecordRepository(repositoryImpl: ExhibitRecordRepositoryImpl) : ExhibitRecordRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl) : ProfileRepository

    @Binds
    @Singleton
    abstract fun bindCategoryManageRepository(repositoryImpl: CategoryManageRepositoryImpl) : CategoryManageRepository

    @Binds
    @Singleton
    abstract fun bindNoticeRepository(repositoryImpl: NoticeRepositoryImpl) : NoticeRepository
}
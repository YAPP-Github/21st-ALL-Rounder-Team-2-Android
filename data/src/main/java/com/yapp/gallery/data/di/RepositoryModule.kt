package com.yapp.gallery.data.di

import com.yapp.gallery.data.repository.CategoryManageRepositoryImpl
import com.yapp.gallery.data.repository.ExhibitRecordRepositoryImpl
import com.yapp.gallery.data.repository.LoginRepositoryImpl
import com.yapp.gallery.data.repository.ProfileRepositoryImpl
import com.yapp.gallery.domain.repository.CategoryManageRepository
import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import com.yapp.gallery.domain.repository.LoginRepository
import com.yapp.gallery.domain.repository.ProfileRepository
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
}
package com.yapp.gallery.data.di

import com.yapp.gallery.data.repository.LoginRepositoryImpl
import com.yapp.gallery.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindLoginRepository(
        repository: LoginRepositoryImpl
    ) : LoginRepository
}
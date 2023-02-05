package com.yapp.gallery.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.yapp.gallery.data.room.TempPostDao
import com.yapp.gallery.data.room.TempPostDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideTempPostDataBase(@ApplicationContext context: Context)
        = Room.databaseBuilder(context, TempPostDatabase::class.java, "artie.db").build()

    @Provides
    @Singleton
    fun provideTempPostDao(tempPostDatabase: TempPostDatabase) : TempPostDao {
        return tempPostDatabase.tempPostDao()
    }
}
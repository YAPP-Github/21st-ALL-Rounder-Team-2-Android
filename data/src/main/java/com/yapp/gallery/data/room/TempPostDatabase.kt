package com.yapp.gallery.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TempPost::class],
    version = 1
)
abstract class TempPostDatabase : RoomDatabase(){
    abstract fun tempPostDao(): TempPostDao
}
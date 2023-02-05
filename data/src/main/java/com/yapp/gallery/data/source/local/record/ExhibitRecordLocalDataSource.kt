package com.yapp.gallery.data.source.local.record

import com.yapp.gallery.data.room.TempPost
import kotlinx.coroutines.flow.Flow

interface ExhibitRecordLocalDataSource {
    fun insertTempPost(
        postId: Long, name: String, categoryId: Long, postDate: String, postLink: String?,
    ): Flow<Long>
    fun updateTempPost(
        postId: Long, name: String, categoryId: Long, postDate: String, postLink: String?,
    ): Flow<Long>

    fun getTempPost() : Flow<TempPost>
    fun deleteTempPost() : Flow<Unit>
}
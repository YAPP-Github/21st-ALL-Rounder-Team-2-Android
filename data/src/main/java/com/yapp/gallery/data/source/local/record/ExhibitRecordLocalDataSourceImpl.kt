package com.yapp.gallery.data.source.local.record

import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.room.TempPost
import com.yapp.gallery.data.room.TempPostDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExhibitRecordLocalDataSourceImpl @Inject constructor(
    private val tempPostDao: TempPostDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ExhibitRecordLocalDataSource {
    override fun insertTempPost(
        postId: Long, name: String, categoryId: Long, postDate: String, postLink: String?
    ) : Flow<Unit> = flow {
        emit(tempPostDao.insertTempPost(TempPost(postId, name, categoryId, postDate, postLink)))
    }.flowOn(dispatcher)
}
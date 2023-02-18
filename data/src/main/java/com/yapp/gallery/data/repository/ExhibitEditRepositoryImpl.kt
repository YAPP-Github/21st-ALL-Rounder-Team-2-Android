package com.yapp.gallery.data.repository

import com.yapp.gallery.data.source.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.domain.repository.ExhibitEditRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExhibitEditRepositoryImpl @Inject constructor(
    private val exhibitRecordRemoteDataSource: ExhibitRecordRemoteDataSource
) : ExhibitEditRepository{
    override fun updateRemotePost(
        postId: Long,
        name: String,
        categoryId: Long,
        postDate: String,
        postLink: String?,
    ): Flow<Boolean> {
        return exhibitRecordRemoteDataSource.updateRecord(postId, name, categoryId, postDate, postLink)
    }

    override fun deleteRemotePost(postId: Long): Flow<Boolean> {
        return exhibitRecordRemoteDataSource.deleteRecord(postId)
    }
}
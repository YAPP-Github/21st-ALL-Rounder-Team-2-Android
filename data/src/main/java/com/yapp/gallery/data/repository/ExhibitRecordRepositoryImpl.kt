package com.yapp.gallery.data.repository

import com.yapp.gallery.data.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExhibitRecordRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExhibitRecordRemoteDataSource
) : ExhibitRecordRepository{
    override fun getCategoryList(): Flow<List<CategoryItem>> {
        return remoteDataSource.getCategoryList()
    }

    override fun createCategory(category: String): Flow<Long> {
        return remoteDataSource.createCategory(category)
    }

    override fun createRecord(name: String, categoryId: Long, postDate: String): Flow<Long> {
        return remoteDataSource.createRecord(name, categoryId, postDate)
    }
}
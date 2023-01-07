package com.yapp.gallery.data.repository

import com.yapp.gallery.data.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import javax.inject.Inject

class ExhibitRecordRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExhibitRecordRemoteDataSource
) : ExhibitRecordRepository{
    override suspend fun getCategoryList(): List<CategoryItem> {
        return remoteDataSource.getCategoryList()
    }

    override suspend fun createCategory(category: String): Long {
        return remoteDataSource.createCategory(category)
    }

    override suspend fun createRecord(name: String, categoryId: Long, postDate: String): Long {
        return remoteDataSource.createRecord(name, categoryId, postDate)
    }
}
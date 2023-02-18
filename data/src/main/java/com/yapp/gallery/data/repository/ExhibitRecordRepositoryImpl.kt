package com.yapp.gallery.data.repository

import com.yapp.gallery.data.source.local.record.ExhibitRecordLocalDataSource
import com.yapp.gallery.data.source.remote.record.ExhibitRecordRemoteDataSource
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.home.TempPostInfo
import com.yapp.gallery.domain.repository.ExhibitRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExhibitRecordRepositoryImpl @Inject constructor(
    private val localDataSource: ExhibitRecordLocalDataSource,
    private val remoteDataSource: ExhibitRecordRemoteDataSource
) : ExhibitRecordRepository{
    override fun getCategoryList(): Flow<List<CategoryItem>> {
        return remoteDataSource.getCategoryList()
    }

    override fun createCategory(category: String): Flow<Long> {
        return remoteDataSource.createCategory(category)
    }

    override fun createRecord(
        name: String, categoryId: Long, postDate: String, attachedLink: String?
    ): Flow<Long> {
        return remoteDataSource.createRecord(name, categoryId, postDate, attachedLink).flatMapMerge {
            localDataSource.insertTempPost(it, name, categoryId, postDate, attachedLink)
        }
    }

    override fun updateBoth(
        postId: Long,
        name: String,
        categoryId: Long,
        postDate: String,
        postLink: String?,
    ): Flow<Long> {
        return remoteDataSource.updateRecord(postId, name, categoryId, postDate, postLink).flatMapMerge {
            localDataSource.updateTempPost(postId, name, categoryId, postDate, postLink)
        }
    }


    override fun getTempPost(): Flow<TempPostInfo> {
        return localDataSource.getTempPost().map {
                p -> TempPostInfo(p.postId, p.name, p.categoryId, p.postDate, p.postLink) }
    }

    override fun deleteTempPost(): Flow<Long> {
        return localDataSource.deleteTempPost()
    }

    override fun deleteBoth(): Flow<Boolean> {
        // 로컬에 있는지 먼저 판단하고 지우기
        return localDataSource.deleteTempPost().flatMapConcat {
            remoteDataSource.deleteRecord(it)
        }
    }
}
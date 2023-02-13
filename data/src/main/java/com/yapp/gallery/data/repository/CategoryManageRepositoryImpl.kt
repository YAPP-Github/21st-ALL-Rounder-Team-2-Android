package com.yapp.gallery.data.repository

import com.yapp.gallery.data.source.remote.category.CategoryManageRemoteDataSource
import com.yapp.gallery.domain.entity.category.CategoryPost
import com.yapp.gallery.domain.repository.CategoryManageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryManageRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryManageRemoteDataSource
) : CategoryManageRepository{
    override fun getCategoryPost(
        pageNum: Int, size: Int, categoryId: Long
    ): Flow<CategoryPost> {
        return remoteDataSource.getCategoryPost(pageNum, size, categoryId)
    }
}
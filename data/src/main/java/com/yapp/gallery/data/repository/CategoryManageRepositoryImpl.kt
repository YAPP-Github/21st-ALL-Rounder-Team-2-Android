package com.yapp.gallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yapp.gallery.data.di.DispatcherModule
import com.yapp.gallery.data.source.remote.category.CategoryManageRemoteDataSource
import com.yapp.gallery.data.source.remote.category.CategoryManagePagingSource
import com.yapp.gallery.domain.entity.category.PostContent
import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.repository.CategoryManageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryManageRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryManageRemoteDataSource,
) : CategoryManageRepository{
    @Inject lateinit var categoryManagePagingFactory: CategoryManagePagingSource.CategoryManagePagingFactory
    override fun getCategoryPost(
        categoryId: Long
    ): Flow<PagingData<PostContent>> {
        return Pager(
            config = PagingConfig(pageSize = CategoryManagePagingSource.PAGE_SIZE),
            pagingSourceFactory = { categoryManagePagingFactory.create(categoryId)},
        ).flow
    }

    override fun editCategory(
        categoryId: Long, editedName: String
    ): Flow<Boolean> {
        return remoteDataSource.editCategory(categoryId, editedName)
    }

    override fun deleteCategory(
        categoryId: Long
    ): Flow<Boolean> {
        return remoteDataSource.deleteCategory(categoryId)
    }

    override fun editCategorySequence(
        categoryList: List<CategoryItem>
    ): Flow<Boolean> {
        return remoteDataSource.editCategorySequence(categoryList)
    }
}
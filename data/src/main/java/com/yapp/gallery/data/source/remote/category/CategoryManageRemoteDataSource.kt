package com.yapp.gallery.data.source.remote.category

import com.yapp.gallery.domain.entity.category.CategoryPost
import kotlinx.coroutines.flow.Flow

interface CategoryManageRemoteDataSource {
    fun getCategoryPost(pageNum: Int, size: Int, categoryId: Long): Flow<CategoryPost>
}
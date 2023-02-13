package com.yapp.gallery.domain.repository

import com.yapp.gallery.domain.entity.category.CategoryPost
import kotlinx.coroutines.flow.Flow

interface CategoryManageRepository {
    fun getCategoryPost(pageNum : Int, size : Int,  categoryId: Long) : Flow<CategoryPost>
}
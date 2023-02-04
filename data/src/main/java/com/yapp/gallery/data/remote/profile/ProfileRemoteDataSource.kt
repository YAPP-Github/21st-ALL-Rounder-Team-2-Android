package com.yapp.gallery.data.remote.profile

import com.yapp.gallery.data.model.UserResponse
import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.flow.Flow

interface ProfileRemoteDataSource {
    fun loadUserData() : Flow<UserResponse>
    fun editCategory(categoryId: Long, editedName: String) : Flow<Boolean>
    fun deleteCategory(categoryId: Long) : Flow<Boolean>
    fun editCategorySequence(categoryList: List<CategoryItem>) : Flow<Boolean>
}
package com.yapp.gallery.data.source.remote.profile

import com.yapp.gallery.domain.entity.home.CategoryItem
import com.yapp.gallery.domain.entity.profile.User
import kotlinx.coroutines.flow.Flow

interface ProfileRemoteDataSource {
    fun loadUserData() : Flow<User>
    fun editCategory(categoryId: Long, editedName: String) : Flow<Boolean>
    fun deleteCategory(categoryId: Long) : Flow<Boolean>
    fun editCategorySequence(categoryList: List<CategoryItem>) : Flow<Boolean>
}
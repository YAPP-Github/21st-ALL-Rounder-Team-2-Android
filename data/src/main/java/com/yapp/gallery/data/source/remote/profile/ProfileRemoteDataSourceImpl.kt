package com.yapp.gallery.data.source.remote.profile

import com.yapp.gallery.data.api.ArtieSerivce
import com.yapp.gallery.data.di.DispatcherModule.IoDispatcher
import com.yapp.gallery.data.model.CategoryBody
import com.yapp.gallery.data.model.UserResponse
import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val artieSerivce: ArtieSerivce,
    @IoDispatcher private val dispatcher : CoroutineDispatcher
) : ProfileRemoteDataSource {
    override fun loadUserData(): Flow<UserResponse> = flow {
        emit(artieSerivce.getUserData())
    }.flowOn(dispatcher)

    override fun editCategory(
        categoryId: Long, editedName: String
    ): Flow<Boolean> = flow {
        emit(artieSerivce.editCategory(categoryId, CategoryBody(editedName)).isSuccessful)
    }.flowOn(dispatcher)

    override fun deleteCategory(
        categoryId: Long
    ): Flow<Boolean> = flow {
        emit(artieSerivce.deleteCategory(categoryId).isSuccessful)
    }.flowOn(dispatcher)

    override fun editCategorySequence(
        categoryList: List<CategoryItem>
    ): Flow<Boolean> = flow {
        emit(artieSerivce.changeCategorySequence(categoryList.sortedBy {c -> c.sequence}).isSuccessful)
    }.flowOn(dispatcher)
}
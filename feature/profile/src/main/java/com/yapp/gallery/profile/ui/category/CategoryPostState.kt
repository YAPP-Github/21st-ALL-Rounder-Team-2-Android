package com.yapp.gallery.profile.ui.category

import androidx.paging.PagingData
import com.yapp.gallery.domain.entity.category.CategoryPost
import com.yapp.gallery.domain.entity.category.PostContent
import com.yapp.gallery.domain.entity.home.CategoryItem
import kotlinx.coroutines.flow.Flow

sealed class CategoryPostState{
    data class Initial(val postFlow: Flow<PagingData<PostContent>>) : CategoryPostState()
    data class Expanded(val postFlow : Flow<PagingData<PostContent>>) : CategoryPostState()
    data class UnExpanded(val postFlow: Flow<PagingData<PostContent>>) : CategoryPostState()
}

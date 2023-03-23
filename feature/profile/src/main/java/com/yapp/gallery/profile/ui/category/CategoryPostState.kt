package com.yapp.gallery.profile.ui.category

import com.yapp.gallery.domain.entity.category.CategoryPost
import com.yapp.gallery.domain.entity.home.CategoryItem

sealed class CategoryPostState{
    data class Initial (val categoryItem: CategoryItem) : CategoryPostState()
    data class Expanded(val categoryPost : CategoryPost) : CategoryPostState()
    data class UnExpanded(val categoryPost: CategoryPost) : CategoryPostState()
}

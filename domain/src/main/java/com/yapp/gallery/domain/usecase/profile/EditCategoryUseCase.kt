package com.yapp.gallery.domain.usecase.profile

import com.yapp.gallery.domain.repository.ProfileRepository
import javax.inject.Inject

class EditCategoryUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(categoryId: Long, editedName: String) = repository.editCategory(categoryId, editedName)
}
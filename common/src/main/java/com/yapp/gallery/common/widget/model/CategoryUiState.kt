package com.yapp.gallery.common.widget.model

sealed class CategoryUiState{
    object Success : CategoryUiState()
    object Empty : CategoryUiState()
    data class Error(val error: String) : CategoryUiState()
}
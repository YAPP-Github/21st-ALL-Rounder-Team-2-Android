package com.yapp.gallery.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getRefreshedToken() : Flow<String?>
}
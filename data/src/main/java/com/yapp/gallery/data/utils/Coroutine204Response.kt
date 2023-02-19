package com.yapp.gallery.data.utils

internal suspend fun completableResponse(block: suspend () -> Unit) = runCatching {
    block.invoke()
}.fold(
    onSuccess = {
        Result.success(Unit)
    },
    onFailure = {
        if(it is KotlinNullPointerException) {
            Result.success(Unit)
        } else {
            Result.failure(it)
        }
    }
)

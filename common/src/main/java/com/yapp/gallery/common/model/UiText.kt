package com.yapp.gallery.common.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

// ViewModel에서 stringResource를 사용하기 위해 만ㄷ름
sealed class UiText{
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString() : String{
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context) : String{
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}

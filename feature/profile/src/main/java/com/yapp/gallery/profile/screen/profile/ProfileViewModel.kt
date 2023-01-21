package com.yapp.gallery.profile.screen.profile

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private var _loginType = MutableStateFlow("")
    val loginType : StateFlow<String>
        get() = _loginType

    fun logout(){
        val type = sharedPreferences.getString("loginType", "").toString()
        sharedPreferences.edit().apply {
            remove("idToken")
            remove("loginType")
        }.apply()
        _loginType.value = type
    }
}
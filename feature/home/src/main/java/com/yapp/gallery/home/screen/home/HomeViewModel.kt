package com.yapp.gallery.home.screen.home

import android.content.SharedPreferences
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    // Web View State
    val webViewState = WebViewState(
        WebContent.Url(
            url = "https://21st-all-rounder-team-2-web-bobeenlee.vercel.app/home",
            additionalHttpHeaders = emptyMap()
        )
    )
}
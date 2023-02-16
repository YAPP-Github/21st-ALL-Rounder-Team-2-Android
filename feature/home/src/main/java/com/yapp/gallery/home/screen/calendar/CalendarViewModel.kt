package com.yapp.gallery.home.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(

) : ViewModel() {
    val webViewState = WebViewState(
        WebContent.Url(
            url = "https://21st-all-rounder-team-2-web-bobeenlee.vercel.app/calendar",
            additionalHttpHeaders = emptyMap()
        )
    )

    val webViewNavigator = WebViewNavigator(viewModelScope)
}
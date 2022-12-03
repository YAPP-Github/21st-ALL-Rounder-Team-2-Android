package com.yapp.gallery.home.navigation

import android.content.Context
import android.content.Intent
import com.yapp.gallery.home.screen.HomeActivity
import com.yapp.gallery.navigation.home.HomeNavigator
import javax.inject.Inject

class HomeNavigatorImpl @Inject constructor(): HomeNavigator {
    override fun navigate(context: Context) : Intent {
        return Intent(context, HomeActivity::class.java)
    }
}
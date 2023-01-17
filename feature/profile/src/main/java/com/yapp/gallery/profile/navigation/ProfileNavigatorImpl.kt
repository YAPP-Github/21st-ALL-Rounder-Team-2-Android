package com.yapp.gallery.profile.navigation

import android.content.Context
import android.content.Intent
import com.yapp.gallery.navigation.profile.ProfileNavigator
import com.yapp.gallery.profile.screen.ProfileActivity
import javax.inject.Inject

class ProfileNavigatorImpl @Inject constructor() : ProfileNavigator {
    override fun navigate(context: Context): Intent {
        return Intent(context, ProfileActivity::class.java)
    }
}
package com.yapp.gallery.login.navigation

import android.content.Context
import android.content.Intent
import com.yapp.gallery.login.LoginActivity
import com.yapp.gallery.navigation.login.LoginNavigator
import javax.inject.Inject

class LoginNavigatorImpl @Inject constructor() : LoginNavigator {
    override fun navigate(context: Context): Intent {
        return Intent(context, LoginActivity::class.java)
    }
}
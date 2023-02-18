package com.yapp.gallery.navigation.info

import android.content.Context
import android.content.Intent
import com.yapp.gallery.core.Navigator

interface ExhibitInfoNavigator : Navigator {
    fun navigateToInfo(context: Context, exhibitId: Long) : Intent
}
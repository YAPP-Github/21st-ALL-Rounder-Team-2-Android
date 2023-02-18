package com.yapp.gallery.info.navigation

import android.content.Context
import android.content.Intent
import com.yapp.gallery.info.screen.info.ExhibitInfoActivity
import com.yapp.gallery.navigation.info.ExhibitInfoNavigator
import javax.inject.Inject

class ExhibitInfoNavigatorImpl @Inject constructor() : ExhibitInfoNavigator {
    override fun navigate(context: Context): Intent {
        return Intent(context, ExhibitInfoActivity::class.java)
    }

    override fun navigateToInfo(context: Context, exhibitId: Long): Intent {
        return Intent(context, ExhibitInfoActivity::class.java).putExtra("exhibitId", exhibitId)
    }

}
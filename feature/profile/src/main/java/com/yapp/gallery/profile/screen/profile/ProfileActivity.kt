package com.yapp.gallery.profile.screen.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.profile.screen.category.CategoryManageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                ProfileScreen(popBackStack = { finish() }, navigateToManage = { navigateToManage() })
            }
        }
    }

    private fun navigateToManage(){
        startActivity(Intent(this, CategoryManageActivity::class.java))
    }
}
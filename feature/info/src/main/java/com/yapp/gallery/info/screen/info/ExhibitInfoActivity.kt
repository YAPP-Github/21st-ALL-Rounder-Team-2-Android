package com.yapp.gallery.info.screen.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yapp.gallery.common.theme.GalleryTheme
import com.yapp.gallery.info.navigation.ExhibitInfoNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExhibitInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                ExhibitInfoNavHost(context = this)
            }
        }
    }
}
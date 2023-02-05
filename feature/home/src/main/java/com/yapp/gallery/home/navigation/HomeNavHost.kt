package com.yapp.gallery.home.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yapp.gallery.home.screen.home.HomeScreen
import com.yapp.gallery.home.screen.record.ExhibitRecordScreen
import com.yapp.gallery.navigation.profile.ProfileNavigator
import com.yapp.navigation.camera.CameraNavigator

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
    profileNavigator: ProfileNavigator,
    cameraNavigator: CameraNavigator
){
    val context = LocalContext.current
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home"){ HomeScreen(
            navigateToRecord = { navHostController.navigate("record")},
            navigateToProfile = { navigateToScreen(context, profileNavigator.navigate(context))}
        ) }
        composable("record"){ ExhibitRecordScreen(
            navController = navHostController,
            navigateToCamera = { navigateToScreen(context, cameraNavigator.navigate(context)) },
            // Todo : 임시로 갤러리 대신 프로필로 가게 함
            navigateToGallery = { navigateToScreen(context, profileNavigator.navigate(context)) }
        ) }
    }
}

fun navigateToScreen(context: Context, intent: Intent){
    context.startActivity(intent)
}
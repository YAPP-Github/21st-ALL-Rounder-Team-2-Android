package com.yapp.gallery.info.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.gallery.info.screen.edit.ExhibitEditScreen
import com.yapp.gallery.info.screen.info.ExhibitInfoScreen
import com.yapp.gallery.navigation.home.HomeNavigator
import com.yapp.navigation.camera.CameraNavigator

@Composable
fun ExhibitInfoNavHost(
    cameraNavigator: CameraNavigator,
    homeNavigator: HomeNavigator,
    context: Activity
){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = "info"){
        composable("info"){
            ExhibitInfoScreen(
                navigateToGallery = {},
                navigateToCamera = {}
            )
        }
        composable("edit"){
            ExhibitEditScreen(
                navigateToHome = {
                    context.finishAffinity()
                    navigateToScreen(context, homeNavigator.navigate(context))
                },
                popBackStack = { popBackStack(context, navHostController)}
            )
        }
    }
}

private fun navigateToScreen(context: Context, intent: Intent){
    context.startActivity(intent)
}

private fun popBackStack(
    context: Activity, navHostController: NavHostController
){
    if (navHostController.previousBackStackEntry != null) {
        navHostController.popBackStack()
    }
    else{
        context.finish()
    }
}
package com.yapp.gallery.info.navigation

import android.app.Activity
<<<<<<< HEAD
import android.content.Context
import android.content.Intent
=======
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.gallery.info.screen.edit.ExhibitEditScreen
import com.yapp.gallery.info.screen.info.ExhibitInfoScreen
<<<<<<< HEAD
import com.yapp.gallery.navigation.home.HomeNavigator
import com.yapp.navigation.camera.CameraNavigator

@Composable
fun ExhibitInfoNavHost(
    cameraNavigator: CameraNavigator,
    homeNavigator: HomeNavigator,
=======

@Composable
fun ExhibitInfoNavHost(
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
    context: Activity
){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = "info"){
        composable("info"){
<<<<<<< HEAD
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
=======
            ExhibitInfoScreen()
        }
        composable("edit"){
            ExhibitEditScreen(
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
                popBackStack = { popBackStack(context, navHostController)}
            )
        }
    }
}

<<<<<<< HEAD
private fun navigateToScreen(context: Context, intent: Intent){
    context.startActivity(intent)
}

=======
>>>>>>> 6ed6ef1 ([ Feature ] : 전시 정보 화면 구현)
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
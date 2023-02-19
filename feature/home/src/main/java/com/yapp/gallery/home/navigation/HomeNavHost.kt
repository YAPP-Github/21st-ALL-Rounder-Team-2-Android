package com.yapp.gallery.home.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yapp.gallery.home.screen.calendar.CalendarScreen
import com.yapp.gallery.home.screen.home.HomeScreen
import com.yapp.gallery.home.screen.record.ExhibitRecordScreen
import com.yapp.gallery.navigation.info.ExhibitInfoNavigator
import com.yapp.gallery.navigation.profile.ProfileNavigator
import com.yapp.navigation.camera.CameraNavigator

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
    profileNavigator: ProfileNavigator,
    cameraNavigator: CameraNavigator,
    infoNavigator: ExhibitInfoNavigator,
    context: Activity,
    navToImagePicker: () -> Unit,
){
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home"){ HomeScreen(
            navigateToRecord = { navHostController.navigate("record")},
            navigateToProfile = { navigateToScreen(context, profileNavigator.navigate(context))},
            navigateToCalendar = { navHostController.navigate("calendar")},
            navigateToInfo = { navigateToScreen(context, infoNavigator.navigateToInfo(context, it))}
        ) }
        composable("record"){ ExhibitRecordScreen(
            navigateToCamera = { navigateToScreen(context, cameraNavigator.navigate(context)) },
            popBackStack = { popBackStack(context, navHostController)},
            navigateToGallery = { navToImagePicker.invoke() }
        ) }
        composable("record"){ ExhibitRecordScreen(
            navigateToCamera = { navigateToScreen(context, cameraNavigator.navigate(context)) },
            navigateToGallery = { navToImagePicker.invoke() },
            popBackStack = { popBackStack(context, navHostController)}
        ) }
        composable("calendar") { CalendarScreen(

        ) }
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
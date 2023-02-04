package com.yapp.gallery.home.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yapp.gallery.home.screen.ExhibitRecordScreen
import com.yapp.gallery.home.screen.HomeScreen
import com.yapp.gallery.navigation.profile.ProfileNavigator

@Composable
fun HomeNavHost(
    navHostController: NavHostController,
    profileNavigator: ProfileNavigator
){
    val context = LocalContext.current
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home"){ HomeScreen(
            navigateToInfo = { navHostController.navigate("record")},
            navigateToProfile = { navigateToScreen(context, profileNavigator.navigate(context))}
        ) }
        composable("record"){ ExhibitRecordScreen(
            navController = navHostController,
            navigateToCamera = { },
        ) }
    }
}

fun navigateToScreen(context: Context, intent: Intent){
    context.startActivity(intent)
}
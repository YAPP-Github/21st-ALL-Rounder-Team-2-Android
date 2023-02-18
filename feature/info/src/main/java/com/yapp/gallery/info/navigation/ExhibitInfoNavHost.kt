package com.yapp.gallery.info.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.gallery.info.screen.edit.ExhibitEditScreen
import com.yapp.gallery.info.screen.info.ExhibitInfoScreen

@Composable
fun ExhibitInfoNavHost(
    context: Activity
){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = "info"){
        composable("info"){
            ExhibitInfoScreen()
        }
        composable("edit"){
            ExhibitEditScreen(
                popBackStack = { popBackStack(context, navHostController)}
            )
        }
    }
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
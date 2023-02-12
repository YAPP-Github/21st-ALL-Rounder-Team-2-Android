package com.yapp.gallery.profile.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yapp.gallery.common.model.BaseState
import com.yapp.gallery.domain.entity.profile.User
import com.yapp.gallery.profile.screen.category.CategoryManageScreen
import com.yapp.gallery.profile.screen.profile.ProfileScreen

@Composable
fun ProfileNavHost(
    logout : () -> Unit,
    withdrawal : () -> Unit,
    context: Activity
){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = "profile"){
        composable("profile"){
            ProfileScreen(
                navigateToManage = { navHostController.navigate("manage") },
                logout = { logout() },
                withdrawal = { withdrawal() },
                popBackStack = { popBackStack(context, navHostController) },
            )
        }
        composable("manage"){
            CategoryManageScreen(popBackStack = { popBackStack(context, navHostController) })
        }
    }
}

private fun popBackStack(
    context: Activity, navHostController: NavHostController
){
    Log.e("back", navHostController.backQueue.toString())
    if (navHostController.previousBackStackEntry != null) {
        navHostController.popBackStack()
    }
    else{
        context.finish()
    }
}
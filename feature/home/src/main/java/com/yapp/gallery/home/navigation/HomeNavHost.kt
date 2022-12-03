package com.yapp.gallery.home.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yapp.gallery.home.screen.ExhibitInfoScreen
import com.yapp.gallery.home.screen.HomeScreen

@Composable
fun HomeNavHost(
    navHostController: NavHostController
){
    NavHost(navController = navHostController, startDestination = "home"){
        composable("home"){ HomeScreen(
            navigateToInfo = { navHostController.navigate("info")}
        ) }
        composable("info"){ ExhibitInfoScreen(navHostController) }
    }
}
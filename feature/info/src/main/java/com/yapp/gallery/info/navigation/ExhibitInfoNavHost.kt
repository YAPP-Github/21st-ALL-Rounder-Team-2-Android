package com.yapp.gallery.info.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yapp.gallery.info.screen.edit.ExhibitEditScreen
import com.yapp.gallery.info.screen.info.ExhibitInfoScreen
import com.yapp.gallery.navigation.home.HomeNavigator
import com.yapp.navigation.camera.CameraNavigator
import org.json.JSONObject
import java.time.LocalDate

@Composable
fun ExhibitInfoNavHost(
    exhibitId: Long,
    cameraNavigator: CameraNavigator,
    homeNavigator: HomeNavigator,
    context: Activity
){
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = "info"){
        composable("info"){
            ExhibitInfoScreen(
                exhibitId = exhibitId,
                navigateToEdit = { payload -> navigateWithPayload(payload, navHostController) },
                navigateToGallery = {},
                navigateToCamera = {},
                popBackStack = { popBackStack(context, navHostController)},
                context = context
            )
        }
        composable(
            route = "edit?id={id},exhibitName={exhibitName},exhibitDate={exhibitDate},categoryId={categoryId},exhibitLink={exhibitLink}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                },
                navArgument("exhibitName"){
                    type = NavType.StringType
                },
                navArgument("exhibitDate"){
                    type = NavType.StringType
                },
                navArgument("categoryId"){
                    type = NavType.LongType
                },navArgument("exhibitLink"){
                    type = NavType.StringType
                }
            )
        ) {
            ExhibitEditScreen(
                navigateToHome = {
                    context.finishAffinity()
                    navigateToScreen(context, homeNavigator.navigate(context))
                },
                popBackStack = { popBackStack(context, navHostController)},
                context = context
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

private fun navigateWithPayload(
    payload: String,
    navHostController: NavHostController
){
    val json = JSONObject(payload)

    val date = LocalDate.parse(json.getString("postDate"))

    val id = json.getLong("id")
    val exhibitName = json.getString("name")
    val categoryId = json.getLong("categoryId")
    var exhibitLink = json.getString("attachedLink")

    if (exhibitLink == "null") exhibitLink = ""

    val exhibitDate = "${date.year}/${date.monthValue}/${date.dayOfMonth}"

    navHostController.navigate("edit?" + "id=${id}," + "exhibitName=${exhibitName},"
            + "exhibitDate=${exhibitDate}," + "categoryId=${categoryId},"+"exhibitLink=${exhibitLink}")
}
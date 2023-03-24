package com.yapp.gallery.profile.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yapp.gallery.profile.ui.category.CategoryManageScreen
import com.yapp.gallery.profile.ui.legacy.LegacyScreen
import com.yapp.gallery.profile.ui.nickname.NicknameScreen
import com.yapp.gallery.profile.ui.notice.NoticeDetailScreen
import com.yapp.gallery.profile.ui.notice.NoticeScreen
import com.yapp.gallery.profile.ui.profile.ProfileScreen
import com.yapp.gallery.profile.ui.signout.SignOutCompleteScreen
import com.yapp.gallery.profile.ui.signout.SignOutScreen
import timber.log.Timber

@Composable
fun ProfileNavHost(
    navigateToLogin : () -> Unit,
    context: Activity
){
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = "profile"){
        composable("profile"){backStackEntry ->
            ProfileScreen(
                navigateToManage = { navHostController.navigate("manage") },
                navigateToNickname = { nickname -> navHostController.navigate("nickname?nickname=${nickname}")},
                navigateToNotice = { navHostController.navigate("notice")},
                navigateToLegacy = { navHostController.navigate("legacy") },
                navigateToSignOut = { navHostController.navigate("signOut")},
                navigateToLogin = { navigateToLogin() },
                popBackStack = { popBackStack(context, navHostController) },
                editedNicknameData = backStackEntry.savedStateHandle.getLiveData<String>("editedName")
            )
        }
        composable("manage"){
            CategoryManageScreen(popBackStack = { popBackStack(context, navHostController) })
        }
        composable(
            route = "nickname?nickname={nickname}",
            arguments = listOf(
                navArgument("nickname"){
                    type = NavType.StringType
                }
            )
        ){
            NicknameScreen(
                popBackStack = { popBackStack(context, navHostController)},
                nicknameUpdate = { setNicknameArgument(context, navHostController, it) }
            )
        }
        composable("notice"){
            NoticeScreen(
                navigateToDetail = { noticeItem ->
                    navHostController.navigate("noticeDetail?" +
                            "title=${noticeItem.title},"+
                            "content=${noticeItem.contents},"+
                            "date=${noticeItem.date}"
                    )
                },
                popBackStack = { popBackStack(context, navHostController) })
        }
        composable(
            route = "noticeDetail?title={title},content={content},date={date}",
            arguments = listOf(
                navArgument("title"){
                    type = NavType.StringType
                },
                navArgument("content"){
                    type = NavType.StringType
                },
                navArgument("date"){
                    type = NavType.StringType
                }
            )
        ){ entry ->
            val noticeTitle = entry.arguments?.getString("title") ?: ""
            val content = entry.arguments?.getString("content") ?: ""
            val date = entry.arguments?.getString("date") ?: ""

            NoticeDetailScreen(
                noticeTitle = noticeTitle,
                noticeContent = content,
                noticeDate = date,
                popBackStack = { popBackStack(context, navHostController) }
            )
        }
        composable("legacy"){
            LegacyScreen(
                popBackStack = { popBackStack(context, navHostController) },
                navigateToWebPage = { navigateToWebPage(context, it) }
            )
        }
        composable("signOut"){
            SignOutScreen(
                popBackStack = { popBackStack(context, navHostController) },
                signOut = {
                    navHostController.navigate("signOutComplete"){
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("signOutComplete"){
            SignOutCompleteScreen(
                navigateToLogin = { navigateToLogin() }
            )
        }
    }
}

private fun popBackStack(
    context: Activity, navHostController: NavHostController
) {
    Timber.tag("back").e(navHostController.backQueue.toString())
    if (navHostController.previousBackStackEntry != null) {
        navHostController.popBackStack()
    } else {
        context.finish()
    }
}

private fun navigateToWebPage(
    context: Activity, @StringRes linkRes: Int
){
    with(context){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(linkRes))))
    }
}

private fun setNicknameArgument(
    context: Activity,
    navHostController: NavHostController,
    editedNickname: String
){
    navHostController.previousBackStackEntry?.savedStateHandle?.set(
        "editedName",
        editedNickname
    )
    popBackStack(context, navHostController)
}
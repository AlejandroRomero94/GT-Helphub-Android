package com.alejandro.helphub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alejandro.helphub.presentation.chat.ChatScreen
import com.alejandro.helphub.presentation.home.Home
import com.alejandro.helphub.presentation.home.HomeViewModel
import com.alejandro.helphub.presentation.notifications.NotificationsScreen
import com.alejandro.helphub.presentation.profile.EditProfileScreen
import com.alejandro.helphub.presentation.profile.EditSkillScreen
import com.alejandro.helphub.presentation.profile.NewSkillScreen1
import com.alejandro.helphub.presentation.profile.NewSkillScreen2
import com.alejandro.helphub.presentation.profile.ProfileScreen
import com.alejandro.helphub.presentation.profile.ProfileSetupStep1
import com.alejandro.helphub.presentation.profile.ProfileSetupStep2
import com.alejandro.helphub.presentation.profile.ProfileSetupStep3
import com.alejandro.helphub.presentation.profile.ProfileSetupStep4a
import com.alejandro.helphub.presentation.profile.ProfileSetupStep4b
import com.alejandro.helphub.presentation.profile.ProfileSetupStep5
import com.alejandro.helphub.presentation.profile.ProfileViewModel


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(BottomBarScreen.Home.route) {
            Home(homeViewModel)
        }
        composable(BottomBarScreen.Chat.route) {
            ChatScreen()
        }
        composable(BottomBarScreen.Notifications.route) {
            NotificationsScreen()
        }
        composable(
            route = BottomBarScreen.Profile.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id")
            val userId = navBackStackEntry.arguments?.getString("userId")

            ProfileScreen(id = id, userId = userId, profileViewModel,navController = navController)
        }
        composable(BottomBarScreen.ProfileSetupStep1.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileSetupStep1(
                profileViewModel = profileViewModel,
                navController = navController,
                email = email
            )
        }
        composable(BottomBarScreen.ProfileSetupStep2.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileSetupStep2(
                profileViewModel = profileViewModel,
                navController = navController,
                email = email
            )
        }
        composable(BottomBarScreen.ProfileSetupStep3.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileSetupStep3(
                profileViewModel = profileViewModel,
                navController = navController, email = email
            )
        }
        composable(BottomBarScreen.ProfileSetupStep4a.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileSetupStep4a(
                profileViewModel = profileViewModel,
                navController = navController, email = email
            )
        }
        composable(BottomBarScreen.ProfileSetupStep4b.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileSetupStep4b(
                profileViewModel = profileViewModel,
                navController = navController, email = email
            )
        }
        composable(BottomBarScreen.ProfileSetupStep5.route) {backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileSetupStep5(
                profileViewModel = profileViewModel,
                navController = navController,
                email = email
            )
        }
        composable(BottomBarScreen.NewSkillScreen1.route){
            NewSkillScreen1(
               profileViewModel = profileViewModel,
                navController = navController,
            )
        }
        composable(BottomBarScreen.NewSkillScreen2.route){
            NewSkillScreen2(
                profileViewModel = profileViewModel,
                navController=navController
            )
        }
        composable(BottomBarScreen.EditProfileScreen.route,
            arguments = listOf(
                navArgument("id"){type = NavType.StringType},
                navArgument("userId"){type = NavType.StringType}
            )
        ){backStackEntry->
            val id=backStackEntry.arguments?.getString("id")
            val userId=backStackEntry.arguments?.getString("userId")
            EditProfileScreen(
                profileViewModel = profileViewModel,
                navController = navController,
                id=id,
                userId=userId
            )
        }
        composable(BottomBarScreen.EditSkillScreen.route,
            arguments = listOf(
                navArgument("skillId") { type = NavType.StringType } // Argumento que se recibe
            )){backStackEntry->
            val skillId=backStackEntry.arguments?.getString("skillId")
            EditSkillScreen(
                profileViewModel = profileViewModel,
                navController = navController,
                skillId=skillId
            )
        }
    }
}
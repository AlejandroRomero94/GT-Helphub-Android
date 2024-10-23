package com.alejandro.helphub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.internal.composableLambda
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alejandro.helphub.features.auth.presentation.AuthViewModel
import com.alejandro.helphub.features.auth.presentation.LoginScreen
import com.alejandro.helphub.features.auth.presentation.SignUpCredsScreen
import com.alejandro.helphub.features.auth.presentation.SignUpStep1
import com.alejandro.helphub.features.auth.presentation.SignUpStep2
import com.alejandro.helphub.features.auth.presentation.SignUpStep3
import com.alejandro.helphub.features.auth.presentation.SignUpStep4Post
import com.alejandro.helphub.features.auth.presentation.SignUpStep4Skill
import com.alejandro.helphub.features.auth.presentation.SignUpStep5
import com.alejandro.helphub.features.home.presentation.Home
import com.alejandro.helphub.features.splash.presentation.SplashScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    val authViewModel:AuthViewModel= hiltViewModel()
    NavHost(navController = navController, startDestination = "SplashScreen") {

        composable("SplashScreen") {
            SplashScreen(onNavigateToLogin = {
                navController.navigate("LoginScreen") {
                    popUpTo("SplashScreen") { inclusive = true }
                }
            })
        }
        composable("LoginScreen") {
            LoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("SignUpCredsScreen") {
            SignUpCredsScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("SignUpStep1") {
            SignUpStep1(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("SignUpStep2") {
            SignUpStep2(
               authViewModel = authViewModel,
               navController = navController
            )
        }
        composable("SignUpStep3") {
            SignUpStep3(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("SignUpStep4Post") {
            SignUpStep4Post(
                authViewModel = authViewModel,
                navController = navController
            )
        }
        composable("SignUpStep4Skill") {
            SignUpStep4Skill(
                authViewModel =authViewModel,
                navController = navController
            )
        }
        composable("SignUpStep5"){
            SignUpStep5(
                authViewModel=authViewModel,
                navController=navController
            )
        }
        composable("Home"){
            Home( )}

    }
}

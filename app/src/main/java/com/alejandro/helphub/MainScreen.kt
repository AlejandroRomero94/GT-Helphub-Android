package com.alejandro.helphub

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alejandro.helphub.domain.models.ProfileUIState
import com.alejandro.helphub.presentation.home.HomeViewModel
import com.alejandro.helphub.presentation.navigation.BottomBarScreen
import com.alejandro.helphub.presentation.navigation.BottomNavGraph
import com.alejandro.helphub.presentation.navigation.NavigationViewModel
import com.alejandro.helphub.presentation.navigation.RootNavGraphObjects
import com.alejandro.helphub.presentation.navigation.bottomBarIcons
import com.alejandro.helphub.presentation.profile.ProfileViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navigationViewModel: NavigationViewModel,
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    email: String?
) {
    var showPopUp by remember { mutableStateOf(false) }
    val currentDestination =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(bottomBar = {

        if (currentDestination != BottomBarScreen.ProfileSetupStep1.route &&
            currentDestination != BottomBarScreen.ProfileSetupStep2.route &&
            currentDestination != BottomBarScreen.ProfileSetupStep3.route &&
            currentDestination != BottomBarScreen.ProfileSetupStep4a.route &&
            currentDestination != BottomBarScreen.ProfileSetupStep4b.route &&
            currentDestination != BottomBarScreen.ProfileSetupStep5.route &&
            currentDestination != BottomBarScreen.NewSkillScreen1.route &&
            currentDestination != BottomBarScreen.NewSkillScreen2.route &&
            currentDestination != BottomBarScreen.EditProfileScreen.route &&
            currentDestination != BottomBarScreen.EditSkillScreen.route
        ) {
            BottomBar(
                navController = navController,
                navigationViewModel = navigationViewModel,
                onShowPopUp = { showPopUp = true }
            )
        }
    }) { paddingValues ->
        Box(modifier = Modifier.padding(PaddingValues())) {
            BottomNavGraph(
                navController = navController,
                profileViewModel = profileViewModel,
                homeViewModel = homeViewModel
            )
            if (showPopUp) {
                CardPopUp(onCompleteProfile = {
                    showPopUp = false
                    navigationViewModel.resetState()
                    navController.navigate(
                        BottomBarScreen.ProfileSetupStep1.createRoute(
                            email!!
                        )
                    ) {
                        popUpTo(RootNavGraphObjects.MainScreen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    navigationViewModel: NavigationViewModel,
    onShowPopUp: () -> Unit
) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Chat,
        BottomBarScreen.Notifications,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val uiState by navigationViewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ProfileUIState.Success -> {
                val statusCode = state.profile.statusCode
                val id = state.profile.id
                val userId = state.profile.userId.id
                if (statusCode == null) {
                    navController.navigate(
                        BottomBarScreen.Profile.createRoute(
                            id,
                            userId
                        )
                    ) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                } else {
                    onShowPopUp()
                }
            }
            is ProfileUIState.Error -> {
                onShowPopUp()
            }
            else -> {} // Handle other states if needed
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 30.dp)
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    if (currentDestination?.route == screen.route) Text(
                        text = screen.title, fontSize = 12.sp
                    )
                },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    if (screen.route == BottomBarScreen.Profile.route) {
                        navigationViewModel.fetchUserProfile()

                    } else {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = bottomBarIcons[screen.route]
                            ?: Icons.Default.Home,
                        contentDescription = screen.title,
                        modifier = Modifier.offset(y = 8.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}

@Composable
fun CardPopUp(
    onCompleteProfile: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = stringResource(id = R.string.warning_content_description),
                    tint = Color.Red,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.profile_popup_title),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,

                    color = Color(0xFF6A4CCD)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.profile_popup_text1),

                    color = MaterialTheme.colorScheme.primary,

                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.profile_popup_text2),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.profile_popup_text3),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 6.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    onClick = {
                        onCompleteProfile()
                    }) {
                    Text(text = stringResource(id = R.string.profile_popup_button))
                }
            }
        }
    }
}
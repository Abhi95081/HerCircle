package com.example.hercircle.AppUi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hercircle.data.Prefs
import com.example.hercircle.screens.*
import kotlinx.coroutines.launch

object Routes {
    const val Onboarding = "onboarding"
    const val Setup = "setup"
    const val Home = "home"
    const val Calendar = "calendar"
    const val Log = "log"
    const val Settings = "settings"
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HerCircleApp(modifier: Modifier = Modifier, prefs: Prefs) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // Observe onboarding flag and theme
    val isOnboarded by prefs.isOnboarded.collectAsState(initial = false)
    val themeMode by prefs.themeMode.collectAsState(initial = "system")

    // Bottom nav items
    val items = listOf(
        BottomNavItem(Routes.Home, "Home") { Icon(Icons.Default.Home, contentDescription = "Home") },
        BottomNavItem(Routes.Calendar, "Calendar") { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
        BottomNavItem(Routes.Log, "Log") { Icon(Icons.Default.List, contentDescription = "Log") },
        BottomNavItem(Routes.Settings, "Settings") { Icon(Icons.Default.Settings, contentDescription = "Settings") }
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Apply theme dynamically
    HerCircleTheme(themeMode = themeMode) {
        Scaffold(
            topBar = {
                if (currentRoute !in listOf(Routes.Onboarding, Routes.Setup)) {
                    TopAppBar(
                        title = { Text(currentRoute?.replaceFirstChar { it.uppercase() } ?: "HerCircle") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            },
            bottomBar = {
                if (currentRoute in listOf(Routes.Home, Routes.Calendar, Routes.Log, Routes.Settings)) {
                    NavigationBar {
                        items.forEach { item ->
                            NavigationBarItem(
                                icon = item.icon,
                                label = { Text(item.label) },
                                selected = currentRoute == item.route,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = if (isOnboarded) Routes.Home else Routes.Onboarding,
                modifier = modifier.padding(padding)
            ) {
                composable(Routes.Onboarding) {
                    OnboardingScreen(onGetStarted = { navController.navigate(Routes.Setup) })
                }
                composable(Routes.Setup) {
                    SetupScreen(
                        onComplete = {
                            scope.launch { prefs.setOnboarded(true) }
                            navController.navigate(Routes.Home) {
                                popUpTo(Routes.Onboarding) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Routes.Home) { HomeScreen() }
                composable(Routes.Calendar) { CalendarScreen(onBack = { navController.popBackStack() }) }
                composable(Routes.Log) { LogScreen(onBack = { navController.popBackStack() }) }
                composable(Routes.Settings) {
                    SettingsScreen(
                        navController = navController,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

// ui/AppRoot.kt
package com.example.flexydoc.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flexydoc.core.Screen
import com.example.flexydoc.ui.components.AppDrawer
import com.example.flexydoc.ui.components.TopBar
import com.example.flexydoc.ui.screen.about.AboutScreen
import com.example.flexydoc.ui.screen.filepicker.FilePickerScreen
import com.example.flexydoc.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.launch
import com.example.flexydoc.R

/**
 * Корневой Composable, содержащий навигацию через [NavHost] и общий Scaffold с Drawer.
 * Включает:
 *  - [AppDrawer] для боковой навигации между экранами;
 *  - [TopBar] с кнопкой открытия Drawer;
 *  - [NavHost] с тремя маршрутами: FilePicker, Settings и About.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val drawerState  = rememberDrawerState(DrawerValue.Closed)
    val scope        = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState  = drawerState,
        drawerContent = {
            AppDrawer(
                onDestinationClick = { screen ->
                    scope.launch { drawerState.close() }
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.app_name),
                    onNavClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->
            NavHost(
                navController  = navController,
                startDestination = Screen.FilePicker.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Screen.FilePicker.route) {
                    FilePickerScreen()
                }
                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
                composable(Screen.About.route) {
                    AboutScreen()
                }
            }
        }
    }
}

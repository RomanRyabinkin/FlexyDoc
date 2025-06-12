package com.example.flexydoc.ui

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.flexydoc.R
import com.example.flexydoc.core.Screen
import com.example.flexydoc.ui.components.AppDrawer
import com.example.flexydoc.ui.components.TopBar
import com.example.flexydoc.ui.model.FeatureAction
import com.example.flexydoc.ui.model.FeatureCategory
import com.example.flexydoc.ui.screen.actions.ActionsScreen
import com.example.flexydoc.ui.screen.filepicker.FilePickerScreen
import com.example.flexydoc.ui.screen.home.HomeScreen
import com.example.flexydoc.ui.screen.settings.SettingsScreen
import com.example.flexydoc.ui.screen.about.AboutScreen
import com.example.flexydoc.ui.screen.pdf.PdfEditorScreen
import kotlinx.coroutines.launch

/**
 * Корневой Composable приложения FlexyDoc.
 *
 * Оборачивает навигацию в общий [ModalNavigationDrawer] с боковым меню
 * и [Scaffold] с [TopBar], а внутри задаёт [NavHost] со всеми маршрутами:
 *  - [Screen.Home]     — главный экран выбора категории;
 *  - [Screen.Actions]  — выбор действия внутри категории;
 *  - [Screen.Picker]   — выбор файла и запуск конкретного действия;
 *  - [Screen.Settings] — экран настроек;
 *  - [Screen.About]    — экран "О приложении".
 *
 * Самостоятельно управляет состоянием drawer’а и подсветкой активного пункта
 * через текущий `route` из `NavController.currentBackStackEntryAsState()`.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val drawerState   = rememberDrawerState(DrawerValue.Closed)
    val scope         = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute  = backStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState  = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute       = currentRoute,
                onDestinationClick = { screen ->
                    scope.launch { drawerState.close() }
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title    = stringResource(R.string.app_name),
                    onNavClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController    = navController,
                startDestination = Screen.Home.route,
                modifier         = Modifier.padding(innerPadding)
            ) {
                // 1) HomeScreen — выбор категории
                composable(Screen.Home.route) {
                    HomeScreen(
                        onCategorySelected = { category ->
                        navController.navigate(
                            Screen.Actions.createRoute(category::class.simpleName!!)
                        )
                    }
                    )
                }

                // 2) ActionsScreen — выбор операции
                composable(
                    route = Screen.Actions.route,
                    arguments = listOf(
                        navArgument("categoryName") { type = NavType.StringType }
                    )
                ) { backStack ->
                    val catName = backStack.arguments!!.getString("categoryName")!!
                    val category = when (catName) {
                        FeatureCategory.PDF::class.simpleName   -> FeatureCategory.PDF
                        FeatureCategory.Word::class.simpleName  -> FeatureCategory.Word
                        FeatureCategory.Image::class.simpleName -> FeatureCategory.Image
                        else -> FeatureCategory.PDF
                    }
                    ActionsScreen(
                        category         = category,
                        onBack           = { navController.popBackStack() },
                        onActionSelected = { action ->
                            navController.navigate(
                                Screen.Picker.createRoute(
                                    catName,
                                    action::class.simpleName!!
                                )
                            )
                        }
                    )
                }

                // 3) FilePickerScreen — выбор файла и запуск действия
                composable(
                    route = Screen.Picker.route,
                    arguments = listOf(
                        navArgument("categoryName") { type = NavType.StringType },
                        navArgument("actionName")   { type = NavType.StringType }
                    )
                ) { backStack ->
                    val catName = backStack.arguments!!.getString("categoryName")!!
                    val actName = backStack.arguments!!.getString("actionName")!!
                    val category = when (catName) {
                        FeatureCategory.PDF::class.simpleName   -> FeatureCategory.PDF
                        FeatureCategory.Word::class.simpleName  -> FeatureCategory.Word
                        FeatureCategory.Image::class.simpleName -> FeatureCategory.Image
                        else -> FeatureCategory.PDF
                    }
                    val action = when (actName) {
                        FeatureAction.Edit::class.simpleName          -> FeatureAction.Edit
                        FeatureAction.Annotate::class.simpleName      -> FeatureAction.Annotate
                        FeatureAction.Highlight::class.simpleName     -> FeatureAction.Highlight
                        FeatureAction.StrikeThrough::class.simpleName -> FeatureAction.StrikeThrough
                        FeatureAction.Print::class.simpleName         -> FeatureAction.Print
                        else -> FeatureAction.Edit
                    }
                    FilePickerScreen(
                        category = category,
                        action   = action,
                        onBack   = { navController.popBackStack() },
                        onFileSelected = { uri ->
                            val uriString = Uri.encode(uri.toString())
                            navController.navigate(
                                Screen.PdfEditor.createRoute(uriString, action::class.simpleName!!)
                            )
                        }
                    )
                }

                composable(
                    route = Screen.PdfEditor.route,
                    arguments = listOf(
                        navArgument("fileUri")    { type = NavType.StringType },
                        navArgument("actionName") { type = NavType.StringType }
                    )
                ) { back ->
                    val uriString  = back.arguments!!.getString("fileUri")!!
                    val actionName = back.arguments!!.getString("actionName")!!
                    val fileUri    = Uri.parse(Uri.decode(uriString))
                    val action     = when(actionName) {
                        FeatureAction.Edit::class.simpleName          -> FeatureAction.Edit
                        FeatureAction.Annotate::class.simpleName      -> FeatureAction.Annotate
                        FeatureAction.Highlight::class.simpleName     -> FeatureAction.Highlight
                        FeatureAction.StrikeThrough::class.simpleName -> FeatureAction.StrikeThrough
                        FeatureAction.Print::class.simpleName         -> FeatureAction.Print
                        else -> FeatureAction.Edit
                    }
                    PdfEditorScreen(
                        fileUri       = fileUri,
                        initialAction  = action,
                        onBack        = { navController.popBackStack() }
                    )
                }

                // 4) Settings и About
                composable(Screen.Settings.route) { SettingsScreen() }
                composable(Screen.About.route)    { AboutScreen()   }
            }
        }
    }
}


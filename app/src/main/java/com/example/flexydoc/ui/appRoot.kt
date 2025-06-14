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
import com.example.flexydoc.converter.RealPdfConverter
import com.example.flexydoc.ui.components.AppDrawer
import com.example.flexydoc.ui.components.TopBar
import com.example.flexydoc.ui.model.FeatureAction
import com.example.flexydoc.ui.model.FeatureCategory
import com.example.flexydoc.ui.screen.about.AboutScreen
import com.example.flexydoc.ui.screen.actions.ActionsScreen
import com.example.flexydoc.ui.screen.convert.PdfConvertScreen
import com.example.flexydoc.ui.screen.filepicker.FilePickerScreen
import com.example.flexydoc.ui.screen.formatselection.FormatSelectionScreen
import com.example.flexydoc.ui.screen.formatselection.PdfFormat
import com.example.flexydoc.ui.screen.home.HomeScreen
import com.example.flexydoc.ui.screen.pdf.PdfEditorScreen
import com.example.flexydoc.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val drawerState   = rememberDrawerState(DrawerValue.Closed)
    val scope         = rememberCoroutineScope()
    val backEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
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
        ) { padding ->
            NavHost(
                navController    = navController,
                startDestination = Screen.Home.route,
                modifier         = Modifier.padding(padding)
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
                ) { entry ->
                    val catName = entry.arguments!!.getString("categoryName")!!
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
                            if (action == FeatureAction.Convert) {
                                navController.navigate(
                                    Screen.FormatSelection.createRoute(
                                        catName,
                                        action::class.simpleName!!
                                    )
                                )
                            } else {
                                navController.navigate(
                                    Screen.Picker.createRoute(
                                        catName,
                                        action::class.simpleName!!
                                    )
                                )
                            }
                        }
                    )
                }

                // 3) FilePickerScreen — выбор файла для Edit/Annotate/Highlight/StrikeThrough/Print
                composable(
                    route = Screen.Picker.route,
                    arguments = listOf(
                        navArgument("categoryName") { type = NavType.StringType },
                        navArgument("actionName")   { type = NavType.StringType }
                    )
                ) { entry ->
                    val catName = entry.arguments!!.getString("categoryName")!!
                    val actName = entry.arguments!!.getString("actionName")!!
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
                        category       = category,
                        action         = action,
                        onBack         = { navController.popBackStack() }
                    ) { uri ->
                        val encoded = Uri.encode(uri.toString())
                        navController.navigate(
                            Screen.PdfEditor.createRoute(
                                encoded,
                                action::class.simpleName!!
                            )
                        )
                    }
                }

                // 4) FormatSelectionScreen — выбор формата
                composable(
                    route = Screen.FormatSelection.route,
                    arguments = listOf(
                        navArgument("categoryName") { type = NavType.StringType },
                        navArgument("actionName")   { type = NavType.StringType }
                    )
                ) {
                    FormatSelectionScreen(
                        onBack = { navController.popBackStack() },
                        onFormatSelected = { fmt ->
                            navController.navigate(
                                Screen.PickerConvert.createRoute(fmt.ext)
                            )
                        }
                    )
                }

                // 5) FilePickerScreen для конвертации
                composable(
                    route = Screen.PickerConvert.route,
                    arguments = listOf(
                        navArgument("format") { type = NavType.StringType }
                    )
                ) { entry ->
                    val fmtKey = entry.arguments!!.getString("format")!!
                    FilePickerScreen(
                        category       = FeatureCategory.PDF,
                        action         = FeatureAction.Convert,
                        onBack         = { navController.popBackStack() }
                    ) { uri ->
                        val encoded = Uri.encode(uri.toString())
                        navController.navigate(
                            Screen.PdfConvert.createRoute(fmtKey, encoded)
                        )
                    }
                }

                // 6) PdfConvertScreen — конвертация с прогрессом
                composable(
                    route = Screen.PdfConvert.route,
                    arguments = listOf(
                        navArgument("format") { type = NavType.StringType },
                        navArgument("uri")    { type = NavType.StringType }
                    )
                ) { entry ->
                    val fmtKey = entry.arguments!!.getString("format")!!
                    val uriStr = entry.arguments!!.getString("uri")!!
                    val parsed = Uri.parse(Uri.decode(uriStr))
                    val fmt    = PdfFormat.values().first { it.ext == fmtKey }

                    // Передаём параметры позиционно согласно сигнатуре:
                    // PdfConvertScreen(format: PdfFormat, onBack: ()->Unit, converter: PdfConverter = ...)
                    PdfConvertScreen(
                        format = fmt,
                        converter = RealPdfConverter(),
                        onBack = { navController.popBackStack() }
                    )
                }

                // 7) PdfEditorScreen — просмотр/редактирование
                composable(
                    route = Screen.PdfEditor.route,
                    arguments = listOf(
                        navArgument("fileUri")    { type = NavType.StringType },
                        navArgument("actionName") { type = NavType.StringType }
                    )
                ) { entry ->
                    val fileUriStr = entry.arguments!!.getString("fileUri")!!
                    val actName2   = entry.arguments!!.getString("actionName")!!
                    val parsedUri  = Uri.parse(Uri.decode(fileUriStr))
                    val action2    = when (actName2) {
                        FeatureAction.Edit::class.simpleName          -> FeatureAction.Edit
                        FeatureAction.Annotate::class.simpleName      -> FeatureAction.Annotate
                        FeatureAction.Highlight::class.simpleName     -> FeatureAction.Highlight
                        FeatureAction.StrikeThrough::class.simpleName -> FeatureAction.StrikeThrough
                        FeatureAction.Print::class.simpleName         -> FeatureAction.Print
                        else -> FeatureAction.Edit
                    }

                    // По вашей сигнатуре: PdfEditorScreen(fileUri: Uri, initialAction: FeatureAction, onBack: ()->Unit)
                    PdfEditorScreen(
                        fileUri = parsedUri,
                        initialAction = action2,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 8) Settings и About
                composable(Screen.Settings.route) { SettingsScreen() }
                composable(Screen.About.route)    { AboutScreen()   }
            }
        }
    }
}

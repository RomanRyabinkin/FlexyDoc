// core/Screen.kt
package com.example.flexydoc.core

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.flexydoc.R

/**
 * Сущность, представляющая экран (маршрут) в навигационном графе приложения.
 *
 * @property route    Уникальный путь маршрута для [NavHost], по которому идентифицируется экран.
 * @property labelRes Ресурс строки для названия экрана в боковом меню (drawer). По умолчанию 0.
 * @property icon     Векторная иконка для экрана в боковом меню (drawer). По умолчанию null.
 */

sealed class Screen(
    val route: String,
    @StringRes val labelRes: Int = 0,
    val icon: ImageVector? = null
) {
    /** Экран выбора категории */
    object Home : Screen(
        route   = "home",
        labelRes = R.string.nav_home,
        icon     = Icons.Default.Home
    )

    /** Экран выбора действия */
    object Actions : Screen(
        route    = "actions/{categoryName}"
    ) {
        /**
         * Формирует конкретный маршрут для [Actions] с переданным именем категории.
         *
         * @param categoryName Имя категории для передачи в маршрут.
         * @return Строка маршрута, например "actions/PDF".
         */

        fun createRoute(categoryName: String) = "actions/$categoryName"
    }

    /** Экран выбора файла и выполнения действия */
    object Picker : Screen(
        route = "picker/{categoryName}/{actionName}"
    ) {
        /**
         * Формирует конкретный маршрут для [Picker] с переданными именами категории и действия.
         *
         * @param categoryName Имя категории для передачи в маршрут.
         * @param actionName   Имя действия для передачи в маршрут.
         * @return Строка маршрута, например "picker/PDF/Edit".
         */

        fun createRoute(categoryName: String, actionName: String) =
            "picker/$categoryName/$actionName"
    }

    /** Настройки */
    object Settings : Screen(
        route    = "settings",
        labelRes = R.string.nav_settings,
        icon     = Icons.Default.Settings
    )

    /** О приложении */
    object About : Screen(
        route    = "about",
        labelRes = R.string.nav_about,
        icon     = Icons.Default.Info
    )

    object PdfEditor : Screen("pdfEditor/{fileUri}/{actionName}") {
        fun createRoute(fileUri: String, actionName: String) =
            "pdfEditor/$fileUri/$actionName"
    }
}


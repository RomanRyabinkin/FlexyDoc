package com.example.flexydoc.core

/**
 * Набор экранов (маршрутов) приложения для навигации через NavController.
 * @property route Уникальный путь, по которому NavHost идентифицирует экран.
 */

sealed class Screen(val route: String) {
    data object FilePicker : Screen("file_picker")
    data object Settings : Screen("settings")
    data object About : Screen("about")

}
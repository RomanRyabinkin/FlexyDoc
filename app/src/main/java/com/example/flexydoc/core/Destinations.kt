package com.example.flexydoc.core

/**
 * Набор экранов (маршрутов) приложения для навигации через NavController.
 * @property route Уникальный путь, по которому NavHost идентифицирует экран.
 */

sealed class Screen(val route: String) {
    object FilePicker : Screen("file_picker")
    object Settings : Screen("settings")
    object About : Screen("about")

}
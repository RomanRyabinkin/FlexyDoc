package com.example.flexydoc.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Варианты тем приложения
enum class ThemeOption(val label: String) {
    Light("Светлая"),
    Dark("Тёмная"),
    System("Системная")
}

// Варианты языков приложения
enum class LanguageOption(val displayName: String) {
    Russian("Русский"),
    English("English")
}

// Модель состояния экрана (имя должно совпадать с тем, что вы используете в ViewModel)
data class SettingsUiState(
    val currentTheme: ThemeOption = ThemeOption.System,
    val themeOptions: List<ThemeOption> = ThemeOption.values().toList(),
    val currentLanguage: LanguageOption = LanguageOption.Russian,
    val languageOptions: List<LanguageOption> = LanguageOption.values().toList()
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onThemeSelected(option: ThemeOption) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentTheme = option) }
            // repo.saveTheme(option)
        }
    }

    fun onLanguageSelected(option: LanguageOption) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentLanguage = option) }
            // repo.saveLanguage(option)
        }
    }
}

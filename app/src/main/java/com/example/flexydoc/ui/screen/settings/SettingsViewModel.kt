package com.example.flexydoc.ui.screen.settings

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexydoc.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Варианты тем приложения
enum class ThemeOption(@StringRes val labelRes: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    System(R.string.theme_system)
}

// Варианты языков приложения
enum class LanguageOption(@StringRes val labelRes: Int) {
    Russian(R.string.lang_russian),
    English(R.string.lang_english)
}

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

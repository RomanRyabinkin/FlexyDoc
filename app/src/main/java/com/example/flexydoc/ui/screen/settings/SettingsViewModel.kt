package com.example.flexydoc.ui.screen.settings

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexydoc.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Перечисление доступных тем приложения.
 * @property labelRes Ресурс строки с названием темы.
 */

enum class ThemeOption(@StringRes val labelRes: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    System(R.string.theme_system)
}

/**
 * Перечисление доступных языков приложения.
 * @property labelRes Ресурс строки с названием языка.
 */

enum class LanguageOption(@StringRes val labelRes: Int) {
    Russian(R.string.lang_russian),
    English(R.string.lang_english)
}


/**
 * Состояние экрана настроек.
 *
 * @property currentTheme Текущий выбранный вариант темы.
 * @property themeOptions Список всех доступных тем.
 * @property currentLanguage Текущий выбранный язык интерфейса.
 * @property languageOptions Список всех доступных языков.
 */

data class SettingsUiState(
    val currentTheme: ThemeOption = ThemeOption.System,
    val themeOptions: List<ThemeOption> = ThemeOption.values().toList(),
    val currentLanguage: LanguageOption = LanguageOption.Russian,
    val languageOptions: List<LanguageOption> = LanguageOption.values().toList()
)

/**[ViewModel] для экрана настроек. Хранит и управляет [SettingsUiState].*/
class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * Обработчик выбора новой темы.
     * @param option Выбранный вариант темы.
     */
    fun onThemeSelected(option: ThemeOption) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentTheme = option) }
            // repo.saveTheme(option)
        }
    }

    /**
     * Обработчик выбора нового языка.
     * @param option Выбранный вариант языка.
     */

    fun onLanguageSelected(option: LanguageOption) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentLanguage = option) }
            // repo.saveLanguage(option)
        }
    }
}

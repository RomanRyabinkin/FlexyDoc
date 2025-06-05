package com.example.flexydoc.ui.screen.settings

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexydoc.R
import com.example.flexydoc.data.settings.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Варианты темы с ресурсами для лейблов */
enum class ThemeOption(@StringRes val labelRes: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    System(R.string.theme_system)
}

/** Варианты языка с ресурсами для лейблов */
enum class LanguageOption(@StringRes val labelRes: Int) {
    Russian(R.string.lang_russian),
    English(R.string.lang_english)
}

/** UI-состояние экрана настроек */
data class SettingsUiState(
    val currentTheme: ThemeOption = ThemeOption.System,
    val themeOptions: List<ThemeOption> = ThemeOption.values().toList(),
    val currentLanguage: LanguageOption = LanguageOption.Russian,
    val languageOptions: List<LanguageOption> = LanguageOption.values().toList()
)

/**
 * ViewModel экрана настроек.
 * На старте подгружает сохранённое значение темы из DataStore,
 * при изменениях сохраняет новое значение и обновляет [SettingsUiState].
 */

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = PreferencesRepository(application)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        // Подписка на поток из DataStore
        viewModelScope.launch {
            repo.themeOptionFlow.collect { storedName ->
                val theme = runCatching { ThemeOption.valueOf(storedName) }
                    .getOrDefault(ThemeOption.System)
                _uiState.update { it.copy(currentTheme = theme) }
            }
        }
        viewModelScope.launch {
            repo.languageOptionFlow.collect { storedLangName ->
                val language = runCatching { LanguageOption.valueOf(storedLangName) }
                    .getOrDefault(LanguageOption.Russian)
                _uiState.update { it.copy(currentLanguage = language) }
            }
        }
    }

    /**
     * Этот метод запускается внутри composable-скоупа,
     * suspend и сразу ждёт setLanguageOption.
     * После записи — обновляется UI-состояние.
     */

    suspend fun saveLanguageSynchronously(option: LanguageOption) {
        repo.setLanguageOption(option.name)
        _uiState.update { it.copy(currentLanguage = option) }
    }

    /**
     * Обработчик выбора новой темы в UI.
     * Сохраняет её в DataStore и обновляет локальное состояние.
     */
    fun onThemeSelected(option: ThemeOption) {
        viewModelScope.launch {
            repo.setThemeOption(option.name)
            _uiState.update { it.copy(currentTheme = option) }
        }
    }

    /**
     * Обработчик выбора языка. Пока никак не сохраняет —
     * просто обновляет UI-состояние.
     */
    fun onLanguageSelected(option: LanguageOption) {
        viewModelScope.launch {
            repo.setLanguageOption(option.name)
            _uiState.update { it.copy(currentLanguage = option) }
        }
    }
}

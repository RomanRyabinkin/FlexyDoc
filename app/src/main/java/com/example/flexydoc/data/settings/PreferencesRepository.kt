package com.example.flexydoc.data.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_prefs"
private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

/**
 * Репозиторий для работы с пользовательскими настройками,
 * сохранёнными в Preferences DataStore.
 *
 * @param context Контекст приложения для доступа к DataStore.
 */

class PreferencesRepository(private val context: Context) {

    private companion object {

        /**
         * Ключ для хранения выбранного варианта темы.
         */

        val THEME_KEY = stringPreferencesKey("theme_option")
    }

    /**
     * Поток сохранённого значения темы интерфейса.
     * Возвращает имя варианта темы или "System" по умолчанию.
     */

    val themeOptionFlow: Flow<String> = context.dataStore.data
        .map { prefs: Preferences ->
            prefs[THEME_KEY] ?: ThemeOptionName.System.name
        }

    /**
     * Сохраняет выбранный вариант темы в DataStore.
     *
     * @param optionName Имя варианта темы для сохранения.
     */

    suspend fun setThemeOption(optionName: String) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = optionName
        }
    }

    /**
     * Вспомогательный enum с текстовыми именами,
     * чтобы не тащить сюда сам ThemeOption из UI-модуля.
     */
    private enum class ThemeOptionName { Light, Dark, System }
}

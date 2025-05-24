// MainActivity.kt
package com.example.flexydoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flexydoc.ui.AppRoot
import com.example.flexydoc.ui.theme.FlexyDocTheme
import com.example.flexydoc.ui.screen.settings.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Получение ViewModel экрана настроек
            val settingsViewModel: SettingsViewModel = viewModel()
            // Подписка на текущее состояние UI из DS
            val uiState by settingsViewModel.uiState.collectAsState()

            // Передача выбранной темы в корневую тему
            FlexyDocTheme(themeOption = uiState.currentTheme) {
                AppRoot()
            }
        }
    }
}


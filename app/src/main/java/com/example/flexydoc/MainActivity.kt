package com.example.flexydoc

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flexydoc.data.settings.PreferencesRepository
import com.example.flexydoc.ui.AppRoot
import com.example.flexydoc.ui.theme.FlexyDocTheme
import com.example.flexydoc.ui.screen.settings.SettingsViewModel
import com.example.flexydoc.util.LocaleHelper
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val repo = PreferencesRepository(newBase)
        val savedLangName: String = runBlocking {
            repo.languageOptionFlow.first()
        }
        val langCode = when (savedLangName) {
            "English" -> "en"
            "Russian" -> "ru"
            else -> "ru" // на всякий случай дефолт
        }
        val wrappedContext = LocaleHelper.wrap(newBase, langCode)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val uiState by settingsViewModel.uiState.collectAsState()
            FlexyDocTheme(themeOption = uiState.currentTheme) {
                AppRoot()
            }
        }
    }
}


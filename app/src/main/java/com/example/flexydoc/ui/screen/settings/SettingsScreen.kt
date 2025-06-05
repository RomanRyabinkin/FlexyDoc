package com.example.flexydoc.ui.screen.settings

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flexydoc.R
import kotlinx.coroutines.launch

/**
 * Экран настроек приложения, отображает опции выбора темы и языка.
 *
 * @param viewModel Инстанс [SettingsViewModel], предоставляющий текущее состояние экрана
 *                  и методы для обновления настроек.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel()
) {
    // Берём текущее состояние (тема + язык) из ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Получаем context и «приводим» его к Activity, чтобы потом вызвать recreate()
    val context = LocalContext.current
    val activity = remember { (context as? Activity) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Заголовок
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(16.dp))

        // 1. Тема приложения
        Text(
            text = stringResource(R.string.theme_label),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        uiState.themeOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = uiState.currentTheme == option,
                    onClick = {
                        viewModel.onThemeSelected(option)
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(option.labelRes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // 2. Язык приложения
        Text(
            text = stringResource(R.string.language_label),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        // Флаг, управляющий открытием выпадающего меню
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(text = stringResource(uiState.currentLanguage.labelRes))
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                uiState.languageOptions.forEach { lang ->
                    DropdownMenuItem(
                        text = { Text(stringResource(lang.labelRes)) },
                        onClick = {
//                            viewModel.onLanguageSelected(lang)
                            expanded = false
                            scope.launch {
                                viewModel.saveLanguageSynchronously(lang)
                            }
                            // 2) Перезапуск Activity, чтобы заново сработало attachBaseContext
                            activity?.recreate()
                        }
                    )
                }
            }
        }
    }
}

package com.example.flexydoc.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.R
import com.example.flexydoc.core.Screen

/**
 * Боковая навигационная панель (drawer) приложения.
 *
 * @param currentRoute Текущий маршрут (route) из NavController. Используется для подсветки
 *                     активного пункта навигации.
 * @param onDestinationClick Callback, вызываемый при клике на пункт навигации.
 *                            В качестве аргумента передаётся один из объектов [Screen].
 *
 * В списке отображаются экраны:
 *  - [Screen.Home]     — главный экран выбора категории;
 *  - [Screen.Settings] — экран настроек;
 *  - [Screen.About]    — экран «О приложении».
 */

@Composable
fun AppDrawer(
    currentRoute: String?,
    onDestinationClick: (Screen) -> Unit
) {
    val destinations = listOf(Screen.Home, Screen.Settings, Screen.About)

    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
            destinations.forEach { screen ->
                NavigationDrawerItem(
                    label     = { Text(stringResource(screen.labelRes)) },
                    icon      = { screen.icon?.let { Icon(it, contentDescription = null) } },
                    selected  = currentRoute == screen.route,
                    onClick   = { onDestinationClick(screen) }
                )
            }
        }
    }
}

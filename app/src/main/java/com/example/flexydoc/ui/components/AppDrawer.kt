package com.example.flexydoc.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
 * @param onDestinationClick Callback, вызываемый при клике на пункт навигации.
 *        В качестве аргумента передаётся один из маршрутов [Screen].
 */

@Composable
fun AppDrawer(onDestinationClick: (Screen) -> Unit) {
    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.nav_home)) },
                selected = false,
                onClick = { onDestinationClick(Screen.FilePicker) }
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.nav_settings)) },
                selected = false,
                onClick = { onDestinationClick(Screen.Settings) }
            )
            NavigationDrawerItem(
                label = { Text(stringResource(R.string.nav_about)) },
                selected = false,
                onClick = { onDestinationClick(Screen.About) }
            )

        }
    }
}
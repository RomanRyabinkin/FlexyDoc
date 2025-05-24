package com.example.flexydoc.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.example.flexydoc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable

        /**
         * Универсальный TopAppBar с кнопкой «бургер» для открытия Drawer.
         *
         * @param title     Заголовок на тулбаре.
         * @param onNavClick  Логика открытия навигационного меню.
         */
fun TopBar(title: String, onNavClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onNavClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu_description)
                )
            }
        }
    )
}
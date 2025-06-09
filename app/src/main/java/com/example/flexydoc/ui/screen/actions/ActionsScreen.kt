package com.example.flexydoc.ui.screen.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.ui.model.FeatureAction
import com.example.flexydoc.ui.model.FeatureCategory

/**
 * Экран выбора действия внутри выбранной категории документов.
 *
 * @param category       Выбранная категория документов ([FeatureCategory]), для которой отображается список действий.
 * @param onActionSelected Lambda, вызываемая при выборе пользователем одного из действий ([FeatureAction]).
 *                         Получает на вход выбранный объект [FeatureAction].
 * @param onBack         Lambda, вызываемая при нажатии кнопки «Назад» в AppBar для возврата к предыдущему экрану.
 * @param modifier       [Modifier] для внешнего оформления и позиционирования экрана.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsScreen(
    category: FeatureCategory,
    onActionSelected: (FeatureAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        FeatureAction.Edit,
        FeatureAction.Annotate,
        FeatureAction.Highlight,
        FeatureAction.StrikeThrough,
        FeatureAction.Print
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(stringResource(id = category.titleRes)) }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            // только innerPadding
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                // и здесь добавляем свои 16.dp
                .padding(16.dp)
        ) {
            items(actions) { action ->
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onActionSelected(action) },
                    leadingContent = {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    headlineContent = {
                        Text(text = stringResource(id = action.titleRes))
                    }
                )
                Divider()
            }
        }
    }
}

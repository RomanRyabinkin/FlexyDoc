package com.example.flexydoc.ui.components


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.R

/**
 * Универсальная кнопка для выбора файла через SAF.
 *
 * Отображает иконку и кнопку с текстом из ресурсов. Позволяет задавать
 * иконку, текст, состояние доступности и логику клика.
 *
 * @param modifier Модификатор для внешнего оформления компонента.
 * @param icon Векторная иконка, отображаемая над кнопкой. По умолчанию — [Icons.Default.Description].
 * @param textRes Ресурс строки для текста кнопки (например, R.string.choose_file).
 * @param enabled Флаг, определяющий, активна ли кнопка. По умолчанию `true`.
 * @param onClick Лямбда, вызываемая при нажатии на кнопку.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Description,
    @StringRes textRes: Int = R.string.choose_file,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp)
        ) {
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
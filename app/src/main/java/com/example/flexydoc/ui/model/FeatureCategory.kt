package com.example.flexydoc.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.flexydoc.R

/**
 * Сущность, описывающая категорию документов в приложении.
 * Каждая категория имеет строковой ресурс для названия и векторную иконку.
 *
 * @property titleRes ID строкового ресурса для отображаемого названия категории.
 * @property icon     Векторная иконка для визуального представления категории.
 */

sealed class FeatureCategory(
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    object PDF   : FeatureCategory(R.string.pdf,    Icons.Default.Description)
    object Word  : FeatureCategory(R.string.word,   Icons.Default.Article)
    object Image : FeatureCategory(R.string.picture,Icons.Default.Image)
}
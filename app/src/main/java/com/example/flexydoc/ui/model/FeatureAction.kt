package com.example.flexydoc.ui.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.StrikethroughS
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.flexydoc.R

/**
 * Сущность, описывающая возможное действие над документом в приложении.
 * Каждое действие отображается с текстовым заголовком из ресурсов и иконкой.
 *
 * @property titleRes ID строкового ресурса для названия действия.
 * @property icon     Векторная иконка для визуального представления действия.
 */

sealed class FeatureAction(
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    object Edit         : FeatureAction(R.string.pdf_edit,       Icons.Default.Edit)
    object Annotate     : FeatureAction(R.string.pdf_note,       Icons.Default.Note)
    object Highlight    : FeatureAction(R.string.pdf_select,     Icons.Default.Highlight)
    object StrikeThrough: FeatureAction(R.string.pdf_cross_out, Icons.Default.StrikethroughS)
    object Print        : FeatureAction(R.string.pdf_print,      Icons.Default.Print)
}
package com.example.flexydoc.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Определяет набор форм (угловых закруглений) для компонентов Material3 в приложении.
 *
 * @property small  Форма для элементов малого размера (радиус 4.dp).
 * @property medium Форма для элементов среднего размера (радиус 8.dp).
 * @property large  Форма для элементов крупного размера (радиус 12.dp).
 */

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

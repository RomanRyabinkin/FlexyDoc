// ui/theme/Theme.kt
package com.example.flexydoc.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.flexydoc.ui.screen.settings.ThemeOption
import com.example.flexydoc.ui.theme.Shapes

private val DarkColorScheme = darkColorScheme(
    primary   = Purple80,
    secondary = PurpleGrey80,
    tertiary  = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary   = Purple40,
    secondary = PurpleGrey40,
    tertiary  = Pink40
)

/**
 * Корневая тема приложения.
 * @param themeOption Выбранная пользователем тема (Light, Dark или System).
 * @param dynamicColor Включить динамические цвета (Material You) на Android 12+.
 * @param content     Компонент, которому будет применена тема.
 */

@Composable
fun FlexyDocTheme(
    themeOption: ThemeOption,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // если пользователь выбрал «System», то учитывается системный режим,
    // иначе — принудительно светлая/тёмная
    val useDarkTheme: Boolean = when (themeOption) {
        ThemeOption.Light  -> false
        ThemeOption.Dark   -> true
        ThemeOption.System -> isSystemInDarkTheme()
    }

    // динамические цвета на Android 12+
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (useDarkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    } else {
        if (useDarkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme  = colorScheme,
        typography   = Typography,
        shapes       = Shapes,
        content      = content
    )
}

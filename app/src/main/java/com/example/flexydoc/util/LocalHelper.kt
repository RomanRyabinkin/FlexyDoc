package com.example.flexydoc.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleHelper {
    /**
     * Возвращает новый ContextWrapper с нужной локалью,
     * чтобы Android взял строки из папки res/values-xx.
     */

    fun wrap(context: Context, languageCode: String): ContextWrapper {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config: Configuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Для Android 7.0+ используеется setLocale и setLocales
            val cfg = Configuration(context.resources.configuration)
            cfg.setLocale(locale)
            cfg.setLayoutDirection(locale)
            cfg
        } else {
            @Suppress("DEPRECATION")
            val cfg = context.resources.configuration
            @Suppress("DEPRECATION")
            cfg.locale = locale
            @Suppress("DEPRECATION")
            cfg.setLayoutDirection(locale)
            cfg
        }

        val newContext = context.createConfigurationContext(config)
        return ContextWrapper(newContext)
    }
}
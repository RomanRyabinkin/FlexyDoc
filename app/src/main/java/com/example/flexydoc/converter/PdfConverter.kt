package com.example.flexydoc.converter

import android.content.Context
import android.net.Uri
import com.example.flexydoc.ui.screen.formatselection.PdfFormat
import java.io.File

interface PdfConverter {
    /**
     * @param sourceUri – PDF для конвертации
     * @param targetFormat – целевой формат
     * @param onProgress – лямбда, вызывается от 0f до 1f
     * @return файл с результатом конвертации
     */

    suspend fun convert(
        context: Context,
        sourceUri: Uri,
        targetFormat: PdfFormat,
        onProgress: (Float) -> Unit
    ): File
}
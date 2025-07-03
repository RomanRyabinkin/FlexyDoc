package com.example.flexydoc.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun Context.getFileName(uri: Uri): String? {
    contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx != -1) return cursor.getString(idx)
            }
        }
    return null
}

/**
 * Копирует указанный content:// URI во внутренний кэш и возвращает получившийся файл.
 * Вызывать сразу в onResult() контракта, пока права на чтение ещё свежие.
 */
fun copyPdfToCache(
    context: Context,
    uri: Uri,
    fallbackName: String = "document.pdf"
): File {
    val originalName = context.getFileName(uri) ?: fallbackName
    val outFile = File(context.cacheDir, originalName)
    context.contentResolver.openInputStream(uri)!!.use { input ->
        outFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return outFile
}

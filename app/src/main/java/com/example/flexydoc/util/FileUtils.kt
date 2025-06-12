package com.example.flexydoc.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun getFileName(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    var name: String? = null
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst()) {
            name = it.getString(nameIndex)
        }
    }
    return name
}

/**
 * Копирует указанный content:// URI во внутренний кэш и возвращает получившийся файл.
 * Вызывать сразу в onResult() контракта, пока права на чтение ещё свежие.
 */
fun copyPdfToCache(context: Context, uri: Uri): File {
    val outFile = File(context.cacheDir, "opened.pdf")
    context.contentResolver.openInputStream(uri)!!.use { input ->
        outFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return outFile
}

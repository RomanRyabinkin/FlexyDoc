package com.example.flexydoc.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream


/**
 * Сохраняет переданный [file] в системную папку «Загрузки» (Downloads) устройства.
 *
 * Функция корректно работает на Android Q и выше, используя MediaStore Download API,
 * и на более ранних версиях — записывает файл напрямую в публичный каталог
 * `Environment.DIRECTORY_DOWNLOADS`.
 *
 * @receiver Контекст приложения или активности, из которого вызывается сохранение.
 * @param file Локальный файл, который нужно скопировать в «Загрузки».
 * @param mimeType MIME-тип сохраняемого файла (например,
 * `"application/vnd.openxmlformats-officedocument.wordprocessingml.document"` для DOCX).
 * @return `true`, если файл был успешно сохранён в папку «Загрузки», иначе `false`.
 */
fun Context.saveToDownloads(file: File, mimeType: String): Boolean {
    val fileName = file.name
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
            put(MediaStore.MediaColumns.DATA, "$downloadsDir/$fileName")
        }
    }

    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Files.getContentUri("external")
    }

    val resolver = contentResolver
    val item = resolver.insert(collection, values) ?: return false

    resolver.openOutputStream(item).use { out: OutputStream? ->
        FileInputStream(file).use { input ->
            if (out == null) return false
            input.copyTo(out)
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val update = ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        resolver.update(item, update, null, null)
    }
    return true
}
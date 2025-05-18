package com.example.flexydoc.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

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

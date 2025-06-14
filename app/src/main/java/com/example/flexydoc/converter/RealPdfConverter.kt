package com.example.flexydoc.converter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.annotation.WorkerThread
import com.example.flexydoc.ui.screen.formatselection.PdfFormat
import com.example.flexydoc.util.copyPdfToCache
import com.shockwave.pdfium.PdfiumCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Реализация конвертера PDF в различные целевые форматы.
 */
class RealPdfConverter : PdfConverter {

    @WorkerThread
    override suspend fun convert(
        context: Context,
        sourceUri: Uri,
        targetFormat: PdfFormat,
        onProgress: (Float) -> Unit
    ): File = withContext(Dispatchers.IO) {
        // Копируем PDF во внутренний кеш приложения
        val pdfFile = copyPdfToCache(context, sourceUri)

        when (targetFormat) {
            PdfFormat.JPG, PdfFormat.PNG -> {
                val dpi = 150
                val pdfium = PdfiumCore(context)
                val images = mutableListOf<File>()

                ParcelFileDescriptor.open(
                    pdfFile,
                    ParcelFileDescriptor.MODE_READ_ONLY
                ).use { fd ->
                    // Открываем документ и закрываем в finally
                    val doc = pdfium.newDocument(fd)
                    try {
                        val pageCount = pdfium.getPageCount(doc)
                        val outDir = File(context.cacheDir, "conv").apply { mkdirs() }

                        for (i in 0 until pageCount) {
                            pdfium.openPage(doc, i)
                            val width = (pdfium.getPageWidthPoint(doc, i) * dpi / 72).toInt()
                            val height = (pdfium.getPageHeightPoint(doc, i) * dpi / 72).toInt()
                            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                            pdfium.renderPageBitmap(doc, bitmap, i, 0, 0, width, height)

                            val ext = targetFormat.ext
                            val outFile = File(outDir, "page${i + 1}.$ext")
                            outFile.outputStream().use { stream ->
                                bitmap.compress(
                                    if (ext == "png") Bitmap.CompressFormat.PNG
                                    else Bitmap.CompressFormat.JPEG,
                                    90,
                                    stream
                                )
                            }
                            images += outFile
                            onProgress((i + 1) / pageCount.toFloat())
                        }
                    } finally {
                        pdfium.closeDocument(doc)
                    }
                }

                // Возвращаем первый файл
                images.first()
            }

            PdfFormat.DOCX -> {
                onProgress(1f)
                // TODO: реализация через PDFBox + POI XWPF
                throw NotImplementedError("Конвертация в DOCX не реализована")
            }

            PdfFormat.XLSX -> {
                onProgress(1f)
                // TODO: реализация через PDFBox + POI
                throw NotImplementedError("Конвертация в XLSX не реализована")
            }
        }
    }
}
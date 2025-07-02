package com.example.flexydoc.converter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.annotation.WorkerThread
import com.example.flexydoc.R
import com.example.flexydoc.ui.screen.formatselection.PdfFormat
import com.example.flexydoc.util.copyPdfToCache
import com.example.flexydoc.util.getFileName
import com.example.flexydoc.util.saveToDownloads
import com.shockwave.pdfium.PdfiumCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.File
import java.io.FileOutputStream

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
        val originalFileName = context.getFileName(sourceUri) ?: "document.pdf"
        val pdfFile = copyPdfToCache(context, sourceUri, originalFileName)
        val baseName = pdfFile.nameWithoutExtension
        val outDir   = File(context.cacheDir, "conv").apply { if (!exists()) mkdirs() }

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

                // Возврат первого файла
                images.first()
            }

            // === Конвертация в DOCX ===
            PdfFormat.DOCX -> {
                onProgress(0.5f)
                PDDocument.load(pdfFile).use { pd ->
                    val text = PDFTextStripper().getText(pd)
                    XWPFDocument().use { docx ->
                        docx.createParagraph()
                            .createRun()
                            .setText(text)

                        val outFile = File(outDir, "$baseName.docx")
                        FileOutputStream(outFile).use { fos ->
                            docx.write(fos)
                        }
                        onProgress(0.9f)

                        val saved = context.saveToDownloads(
                            outFile,
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )

                        withContext(Dispatchers.Main) {
                            if (saved) {
                                val toastMessage = context.getString(R.string.toast_saved_to_downloads, outFile.name)
                                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Не удалось сохранить файл", Toast.LENGTH_LONG).show()
                            }
                        }

                        onProgress(1f)
                        return@withContext outFile
                    }
                }
            }

            // === Конвертация в XLSX(Excel) ===
            PdfFormat.XLSX -> {
                onProgress(0.5f)
                PDDocument.load(pdfFile).use { pd ->
                    val lines = PDFTextStripper().getText(pd).lines()
                    XSSFWorkbook().use { wb ->
                        val sheet = wb.createSheet("PDF")
                        lines.forEachIndexed { idx, line ->
                            sheet.createRow(idx)
                                .createCell(0)
                                .setCellValue(line)
                        }

                        val outFile = File(outDir, "$baseName.xlsx")
                        FileOutputStream(outFile).use { fos ->
                            wb.write(fos)
                        }
                        onProgress(1f)
                        return@withContext outFile
                    }
                }
            }
        }
    }
}
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
import com.tom_roush.pdfbox.rendering.PDFRenderer
import org.apache.poi.util.Units
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

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
                onProgress(0.1f)
                PDDocument.load(pdfFile).use { pd ->
                    val renderer = PDFRenderer(pd)
                    // Новая книга DOCX
                    XWPFDocument().use { docx ->
                        val stripper = PDFTextStripper()

                        for (page in 0 until pd.numberOfPages) {
                            stripper.startPage = page + 1
                            stripper.endPage   = page + 1
                            val pageText = stripper.getText(pd).trim()
                            if (pageText.isNotEmpty()) {
                                val p = docx.createParagraph()
                                val r = p.createRun()
                                r.setText(pageText)
                            }
                            onProgress(0.1f + (page + 0.5f) / pd.numberOfPages * 0.8f)
                            val bmp: Bitmap = renderer.renderImageWithDPI(page, 150f)
                            val baos = ByteArrayOutputStream().also { out ->
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                            }
                            val picData = baos.toByteArray()

                            val wEmu = Units.pixelToEMU(bmp.width)
                            val hEmu = Units.pixelToEMU(bmp.height)

                            val picPara = docx.createParagraph()
                            val picRun  = picPara.createRun()
                            picRun.addPicture(
                                ByteArrayInputStream(picData),
                                XWPFDocument.PICTURE_TYPE_PNG,
                                "page${page + 1}.png",
                                wEmu,
                                hEmu
                            )

                            onProgress(0.1f + (page + 1f) / pd.numberOfPages * 0.8f)
                        }
                        val outFile = File(outDir, "$baseName.docx")
                        FileOutputStream(outFile).use { fos ->
                            docx.write(fos)
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
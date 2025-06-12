package com.example.flexydoc.ui.screen.pdf

import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.flexydoc.R
import com.example.flexydoc.ui.model.FeatureAction
import com.example.flexydoc.util.copyPdfToCache
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

/**
 * Экран просмотра и редактирования PDF-документа.
 *
 * Если передан content:// URI, копирует его в cacheDir;
 * если передан file:// URI, использует файл напрямую.
 *
 * @param fileUri       URI PDF-файла (content:// или file://).
 * @param initialAction Инструмент, активируемый при старте (редактирование, заметка и т.д.).
 * @param onBack        Лямбда для обработки нажатия "Назад".
 * @param modifier      Модификатор для внешнего оформления.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfEditorScreen(
    fileUri: Uri,
    initialAction: FeatureAction,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentTool by remember { mutableStateOf(initialAction) }

    // Определяем PDF-файл: копируем content:// в cacheDir, file:// оставляем как есть
    val pdfFile: File = remember(fileUri) {
        if (fileUri.scheme == "content") {
            copyPdfToCache(context, fileUri)
        } else {
            File(fileUri.path!!)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.pdf_screen_back)
                        )
                    }
                },
                title = {
                    Text(text = "PDF — ${context.getString(currentTool.titleRes)}")
                }
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    FeatureAction.Edit to Icons.Filled.Edit,
                    FeatureAction.Annotate to Icons.Filled.Note,
                    FeatureAction.Highlight to Icons.Filled.Highlight,
                    FeatureAction.StrikeThrough to Icons.Filled.StrikethroughS,
                    FeatureAction.Print to Icons.Filled.Print
                ).forEach { (action, icon) ->
                    IconButton(onClick = { currentTool = action }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (currentTool == action)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AndroidView(
                factory = { ctx ->
                    PDFView(ctx, null).apply {
                        fromFile(pdfFile)
                            .enableSwipe(true)
                            .enableAnnotationRendering(true)
                            .load()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (currentTool == FeatureAction.Print) {
                LaunchedEffect(pdfFile) {
                    val printManager = context.getSystemService(PrintManager::class.java)
//                    val adapter = PDFPrintDocumentAdapter(context, pdfFile)
//                    printManager.print(
//                        "PDF_Document",
//                        adapter,
//                        PrintAttributes.Builder().build()
//                    )
                }
            }
        }
    }
}
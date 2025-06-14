package com.example.flexydoc.ui.screen.convert

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Internal.ListAdapter.Converter
import com.example.flexydoc.converter.PdfConverter
import com.example.flexydoc.converter.RealPdfConverter
import com.example.flexydoc.ui.screen.formatselection.PdfFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfConvertScreen(
    format: PdfFormat,
    onBack: () -> Unit,
    converter: PdfConverter = remember { RealPdfConverter() }  // абстракция над конвертацией
) {
    val context = LocalContext.current
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    var progress by remember { mutableStateOf(0f) }  // 0f–1f
    var status by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pdfUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                title = { Text("Конвертация в ${format.ext}") }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    pdfUri?.let { src ->
                        scope.launch {
                            // преобразование, обновляем progress
                            val outFile = converter.convert(
                                context = context,
                                sourceUri = src,
                                targetFormat = format,
                                onProgress = { pct -> progress = pct }
                            )
                            status = "Файл ${outFile.name} успешно конвертирован в ${format.ext}"
                            // можно выдать Toast или Snackbar
                        }
                    }
                },
                enabled = pdfUri != null,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Начать конвертацию")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { launcher.launch("application/pdf") }) {
                Text(if (pdfUri==null) "Выберите PDF" else "PDF выбран")
            }
            Spacer(Modifier.height(24.dp))
            if (progress in 0f..1f) {
                LinearProgressIndicator(progress, Modifier.fillMaxWidth())
                Text("${(progress*100).toInt()}%", style = MaterialTheme.typography.bodySmall)
            }
            status?.let {
                Spacer(Modifier.height(24.dp))
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

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
import androidx.compose.material.icons.filled.Description
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.Internal.ListAdapter.Converter
import com.example.flexydoc.R
import com.example.flexydoc.converter.PdfConverter
import com.example.flexydoc.converter.RealPdfConverter
import com.example.flexydoc.ui.components.FilePickerButton
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
    var isConverting by remember { mutableStateOf(false) }
    val status by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null && !isConverting) {
            pdfUri = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                title = {
                    Text(
                        stringResource(
                            R.string.conversion_to_format_title,
                            format.ext
                        )
                    )
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    pdfUri?.let { src ->
                        scope.launch {
                            isConverting = true
                            progress = 0f
                            converter.convert(
                                context     = context,
                                sourceUri   = src,
                                targetFormat= format
                            ) { pct ->
                                progress = pct
                            }
                            isConverting = false
                            pdfUri       = null
                            progress     = 0f
                            // здесь можно показать Snackbar/Toast в будущем
                        }
                    }
                },
                enabled = pdfUri != null && !isConverting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.start_convertation))
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
            FilePickerButton(
                icon    = Icons.Default.Description,
                textRes = if (pdfUri == null) R.string.select_pdf else R.string.pdf_selected,
                enabled = !isConverting,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                launcher.launch("application/pdf")
            }
            Spacer(Modifier.height(24.dp))

            if (isConverting) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

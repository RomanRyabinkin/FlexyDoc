package com.example.flexydoc.ui.screen.formatselection

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.R

enum class PdfFormat(
    @StringRes val labelRes: Int,
    val mime: String,
    val ext: String
) {
    DOCX(
        labelRes = R.string.word_convertation,
        mime     = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        ext      = "docx"
    ),
    XLSX(
        labelRes = R.string.excel_convertation,
        mime     = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        ext      = "xlsx"
    ),
    JPG(
        labelRes = R.string.jpg_convertation,
        mime     = "image/jpeg",
        ext      = "jpg"
    ),
    PNG(
        labelRes = R.string.png_convertation,
        mime     = "image/png",
        ext      = "png"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormatSelectionScreen(
    onBack: () -> Unit,
    onFormatSelected: (PdfFormat) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text(stringResource(R.string.choose_format)) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = PdfFormat.values().toList()) { fmt ->
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFormatSelected(fmt) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    headlineContent = { Text(stringResource(fmt.labelRes)) }
                )
                Divider()
            }
        }
    }
}

package com.example.flexydoc.ui.screen.filepicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.R
import com.example.flexydoc.ui.components.FilePickerButton
import com.example.flexydoc.ui.model.FeatureAction
import com.example.flexydoc.ui.model.FeatureCategory
import com.example.flexydoc.util.copyPdfToCache
import java.io.File

/**
 * Экран выбора файла. Полученный через SAF content:// URI сразу копируется
 * в внутренний кеш и передается дальше в виде локального URI file://.
 *
 * @param modifier       Модификатор для внешнего оформления.
 * @param category       Категория документа, определяет MIME-тип.
 * @param action         Действие (редактирование, печать и т.д.).
 * @param onBack         Лямбда для возврата назад.
 * @param onFileSelected Лямбда, вызываемая после выбора и копирования файла,
 *                       получает локальный URI (file://).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerScreen(
    modifier: Modifier = Modifier,
    category: FeatureCategory,
    action: FeatureAction,
    onBack: () -> Unit,
    onFileSelected: (Uri) -> Unit
) {
    val context = LocalContext.current

    // Контракт GetContent поддерживается на всех устройствах и эмуляторах
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Копируем PDF в кэш приложения
            val cacheFile: File = copyPdfToCache(context, it)
            // Передаём локальный URI
            onFileSelected(Uri.fromFile(cacheFile))
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.pdf_screen_back)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(category.titleRes) + " → " + stringResource(action.titleRes),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            FilePickerButton(
                modifier = Modifier.fillMaxWidth(0.7f),
                onClick = {
                    val mimeType = when (category) {
                        FeatureCategory.PDF   -> "application/pdf"
                        FeatureCategory.Word  -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        FeatureCategory.Image -> "image/*"
                        else                  -> "*/*"
                    }
                    launcher.launch(mimeType)
                }
            )
        }
    }
}
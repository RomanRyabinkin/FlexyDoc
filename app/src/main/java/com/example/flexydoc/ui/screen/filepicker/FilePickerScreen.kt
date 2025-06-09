package com.example.flexydoc.ui.screen.filepicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.flexydoc.util.getFileName
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.example.flexydoc.R
import com.example.flexydoc.ui.model.FeatureAction
import com.example.flexydoc.ui.model.FeatureCategory

/**
 * Экран для выбора файла и выполнения выбранного действия над ним.
 *
 * Позволяет пользователю:
 * 1. Открыть диалог выбора документа с MIME-типами, соответствующими [category].
 * 2. Показать имя выбранного файла и выполнить [action] (конвертация, редактирование, печать и т.д.).
 * 3. Показать Snackbar с результатом операции.
 *
 * @param modifier  [Modifier] для внешнего оформления и позиционирования экрана.
 * @param category  Выбранная категория документа ([FeatureCategory]), определяющая набор MIME-типов для выбора.
 * @param action    Выбранное действие ([FeatureAction]), выполняемое над выбранным документом.
 * @param onBack    Лямбда, вызываемая при нажатии кнопки "Назад" в AppBar для возврата к предыдущему экрану.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerScreen(
    modifier: Modifier = Modifier,
    category: FeatureCategory,
    action: FeatureAction,
    onBack: () -> Unit
                     ) {
    val context = LocalContext.current
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val conversionDoneText = stringResource(R.string.conversion_done)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            selectedFileUri = uri
            selectedFileName = uri?.let { getFileName(context, it) }
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("FlexyDoc", style = MaterialTheme.typography.titleLarge) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Description,
                contentDescription = "File Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    launcher.launch(
                        arrayOf(
                            "application/pdf",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(stringResource(R.string.choose_file), style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedFileName != null) {
                Text(
                    text = "Выбран: $selectedFileName",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(conversionDoneText)
                        }
                    },
                    enabled = selectedFileUri != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Конвертировать", style = MaterialTheme.typography.titleMedium)
                }
            } else {
                Text(
                    text = stringResource(R.string.file_not_chosen),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}


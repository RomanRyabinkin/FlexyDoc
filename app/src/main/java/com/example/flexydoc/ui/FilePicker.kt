package com.example.flexydoc.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.flexydoc.util.getFileName

/**
 * Компонент выбора файла.
 *
 * @param modifier Модификатор для внешнего вида компонента.
 */

@Composable
fun FilePicker(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Состояние для хранения имени выбранного файла.
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    // Состояние для хранения Uri выбранного файла (пригодится для конвертации и предпросмотра).
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    /**
     * Лаунчер для открытия системного диалога выбора файла с фильтрацией по MIME-типам.
     * В данном случае разрешаем только PDF и DOCX.
     */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            selectedFileUri = uri
            selectedFileName = uri?.let { getFileName(context, it) }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Кнопка запуска диалога выбора файла.
        Button(
            onClick = {
                // Фильтруем только PDF и DOCX
                launcher.launch(
                    arrayOf(
                        "application/pdf",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    )
                )
            }
        ) {
            Text("Выбрать файл")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Отображаем имя выбранного файла, если оно есть
        if (selectedFileName != null) {
            Text(
                text = "Выбранный файл: $selectedFileName",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка "Конвертировать" появляется только после выбора файла
            Button(
                onClick = {
                    // Здесь будет конвертация. Пока просто мок-уведомление.
                    // TODO: добавить реальную конвертацию
                },
                enabled = selectedFileUri != null
            ) {
                Text("Конвертировать")
            }
        } else {
            Text("Файл не выбран", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

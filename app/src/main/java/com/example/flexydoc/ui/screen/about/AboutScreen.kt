package com.example.flexydoc.ui.screen.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.R
import java.util.Calendar

/**Экран о "о приложении", показывает иконку, название, версию и copyright*/
@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val version = run {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        info.versionName.orEmpty()
    }
    val year: Int = Calendar.getInstance().get(Calendar.YEAR)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(Modifier.height(24.dp))

        /**Иконка информации*/
        Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(64.dp))

        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.about_version, version),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.about_copyright, year),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
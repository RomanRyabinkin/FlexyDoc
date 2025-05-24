package com.example.flexydoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flexydoc.ui.AppRoot
import com.example.flexydoc.ui.theme.FlexyDocTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlexyDocTheme {
                AppRoot()
            }
        }
    }
}


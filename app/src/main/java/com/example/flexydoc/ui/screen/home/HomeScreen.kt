package com.example.flexydoc.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.flexydoc.R
import com.example.flexydoc.ui.model.FeatureCategory

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCategorySelected: (FeatureCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        FeatureCategory.PDF,
        FeatureCategory.Word,
        FeatureCategory.Image
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
        modifier = modifier
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            // передаём только innerPadding
            contentPadding = innerPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                // отдельно добавляем внешние отступы
                .padding(16.dp)
        ) {
            items(categories) { category ->
                Card(
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = category.titleRes),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}


package com.example.seamlesscarouselcomposer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.seamlesscarouselcomposer.data.ProjectViewModel
import com.example.seamlesscarouselcomposer.ui.components.CompositePreview
import com.example.seamlesscarouselcomposer.ui.components.TransformControlPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(vm: ProjectViewModel) {
    val s by vm.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seamless Composer") },
                actions = { if (s.exporting) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp) }
            )
        },
        containerColor = Color(0xFFF2F2F7)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9EAEE)),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    s.preview?.let {
                        CompositePreview(
                            bitmap = it,
                            pageCount = s.images.size.coerceAtLeast(1),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                TransformControlPanel(vm)
            }

            s.exportResult?.let { ExportScreen(it) }
        }
    }
}

package com.example.seamlesscarouselcomposer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.seamlesscarouselcomposer.data.ProjectViewModel
import com.example.seamlesscarouselcomposer.ui.components.CompositePreview
import com.example.seamlesscarouselcomposer.ui.components.EditorTool
import com.example.seamlesscarouselcomposer.ui.components.TransformControlPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(vm: ProjectViewModel) {
    val s by vm.state.collectAsStateWithLifecycle()
    var selectedTool by remember { mutableStateOf(EditorTool.Move) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seamless Composer") },
                actions = {
                    if (s.previewRendering || s.exporting) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    }
                }
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3E5EA)),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.Center) {
                    if (s.preview == null) {
                        Text("点击刷新预览生成合成图", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    } else {
                        CompositePreview(
                            bitmap = s.preview!!,
                            pageCount = s.images.size.coerceAtLeast(1),
                            modifier = Modifier.fillMaxSize(),
                            onTransformGesture = { pan, zoom, rotation ->
                                vm.updateTransform { tr ->
                                    tr.copy(
                                        offsetX = tr.offsetX + pan.x,
                                        offsetY = tr.offsetY + pan.y,
                                        scale = (tr.scale * zoom).coerceIn(0.5f, 2f),
                                        rotation = (tr.rotation + rotation).coerceIn(-5f, 5f)
                                    )
                                }
                            }
                        )
                        Text(
                            "Drag to move · pinch to zoom · rotate with two fingers",
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color.Black.copy(alpha = 0.24f), MaterialTheme.shapes.small)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    s.images.forEachIndexed { index, image ->
                        AssistChip(
                            onClick = { vm.selectImage(index) },
                            label = { Text(image.displayName) }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                TransformControlPanel(vm = vm, selectedTool = selectedTool, onToolSelected = { selectedTool = it })
            }

            s.exportResult?.let { ExportScreen(it) }
        }
    }
}

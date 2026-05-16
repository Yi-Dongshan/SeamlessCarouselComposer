package com.example.seamlesscarouselcomposer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.seamlesscarouselcomposer.data.ProjectViewModel
import com.example.seamlesscarouselcomposer.model.ExportPreset
import com.example.seamlesscarouselcomposer.ui.components.TransformControlPanel

@Composable
fun EditorScreen(vm: ProjectViewModel) {
    val s by vm.state.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(12.dp).verticalScroll(rememberScrollState())) {
        Text("合成预览")
        s.preview?.let { Image(it.asImageBitmap(), null, modifier = Modifier.fillMaxWidth().height(180.dp)) }
        Row(Modifier.horizontalScroll(rememberScrollState())) {
            s.images.forEachIndexed { i, img -> AssistChip(onClick = { vm.selectImage(i) }, label = { Text(img.displayName) }) }
        }
        TransformControlPanel(vm)
        ExportPreset.entries.forEach { p -> Row { RadioButton(selected = s.preset == p, onClick = { vm.setPreset(p) }); Text(p.label) } }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf(0, 8, 16, 32).forEach { AssistChip(onClick = { vm.setFeather(it) }, label = { Text("羽化 $it") }) } }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.refreshPreview() }) { Text("刷新预览") }
            Button(onClick = { vm.export() }, enabled = !s.exporting) { Text("导出") }
        }
        s.exportResult?.let { ExportScreen(it) }
    }
}

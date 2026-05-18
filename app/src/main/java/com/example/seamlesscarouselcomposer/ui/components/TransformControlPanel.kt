package com.example.seamlesscarouselcomposer.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seamlesscarouselcomposer.data.ProjectViewModel
import com.example.seamlesscarouselcomposer.model.ExportPreset

enum class EditorTool(val label: String) { Move("Move"), Scale("Scale"), Rotate("Rotate"), Export("Export") }

@Composable
fun TransformControlPanel(vm: ProjectViewModel, selectedTool: EditorTool, onToolSelected: (EditorTool) -> Unit, modifier: Modifier = Modifier) {
    val s = vm.state.value
    val t = s.images.getOrNull(s.selectedIndex)?.transform ?: return

    Column(modifier = modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            EditorTool.entries.forEach { tool ->
                FilterChip(selected = selectedTool == tool, onClick = { onToolSelected(tool) }, label = { Text(tool.label) })
            }
        }

        when (selectedTool) {
            EditorTool.Move -> {
                Text("X ${"%.1f".format(t.offsetX)} · Y ${"%.1f".format(t.offsetY)}", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(-10f, -1f, 1f, 10f).forEach { delta ->
                        TextButton(onClick = { vm.nudgeSelectedOffset(dx = delta) }) { Text(if (delta > 0) "+${delta.toInt()}" else delta.toInt().toString()) }
                    }
                }
                Slider(value = t.offsetX, onValueChange = { vm.updateTransform { tr -> tr.copy(offsetX = it) } }, valueRange = -800f..800f)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(-10f, -1f, 1f, 10f).forEach { delta ->
                        TextButton(onClick = { vm.nudgeSelectedOffset(dy = delta) }) { Text(if (delta > 0) "+${delta.toInt()}" else delta.toInt().toString()) }
                    }
                }
                Slider(value = t.offsetY, onValueChange = { vm.updateTransform { tr -> tr.copy(offsetY = it) } }, valueRange = -800f..800f)
            }

            EditorTool.Scale -> {
                Text("Scale ${"%.2f".format(t.scale)}", style = MaterialTheme.typography.labelMedium)
                Slider(value = t.scale, onValueChange = { vm.updateTransform { tr -> tr.copy(scale = it.coerceIn(0.5f, 2f)) } }, valueRange = 0.5f..2f)
            }

            EditorTool.Rotate -> {
                Text("Rotation ${"%.2f".format(t.rotation)}°", style = MaterialTheme.typography.labelMedium)
                Slider(value = t.rotation, onValueChange = { vm.updateTransform { tr -> tr.copy(rotation = it.coerceIn(-5f, 5f)) } }, valueRange = -5f..5f)
            }

            EditorTool.Export -> {
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    ExportPreset.entries.forEach { p ->
                        Row { RadioButton(selected = s.preset == p, onClick = { vm.setPreset(p) }); Text(p.label, modifier = Modifier.padding(end = 8.dp)) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0, 8, 16, 32).forEach { px ->
                        AssistChip(onClick = { vm.setFeather(px) }, label = { Text("Feather $px") })
                    }
                }
                Button(onClick = { vm.export() }, enabled = !s.exporting, modifier = Modifier.fillMaxWidth()) { Text("Export") }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { vm.resetSelectedTransform() }) { Text("Reset Selected") }
            TextButton(onClick = { vm.copySelectedTransformToAll() }) { Text("Copy to All") }
        }
    }
}

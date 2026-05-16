package com.example.seamlesscarouselcomposer.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.seamlesscarouselcomposer.data.ProjectViewModel
import com.example.seamlesscarouselcomposer.model.ExportPreset

@Composable
fun TransformControlPanel(vm: ProjectViewModel, modifier: Modifier = Modifier) {
    val s = vm.state.value
    val t = s.images.getOrNull(s.selectedIndex)?.transform ?: return
    var selectedTab by remember { mutableStateOf("Move") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            s.images.forEachIndexed { i, img ->
                AssistChip(onClick = { vm.selectImage(i) }, label = { Text(img.displayName) })
            }
        }

        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Move", "Scale", "Rotate", "Export").forEach { tab ->
                FilterChip(selected = selectedTab == tab, onClick = { selectedTab = tab }, label = { Text(tab) })
            }
        }

        when (selectedTab) {
            "Move" -> {
                Text("Offset X ${"%.1f".format(t.offsetX)}", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = { vm.nudgeSelectedOffset(dx = -10f) }) { Text("-10") }
                    TextButton(onClick = { vm.nudgeSelectedOffset(dx = -1f) }) { Text("-1") }
                    TextButton(onClick = { vm.nudgeSelectedOffset(dx = 1f) }) { Text("+1") }
                    TextButton(onClick = { vm.nudgeSelectedOffset(dx = 10f) }) { Text("+10") }
                }
                Slider(value = t.offsetX, onValueChange = { vm.updateTransform { tr -> tr.copy(offsetX = it) } }, valueRange = -800f..800f)

                Text("Offset Y ${"%.1f".format(t.offsetY)}", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = { vm.nudgeSelectedOffset(dy = -10f) }) { Text("-10") }
                    TextButton(onClick = { vm.nudgeSelectedOffset(dy = -1f) }) { Text("-1") }
                    TextButton(onClick = { vm.nudgeSelectedOffset(dy = 1f) }) { Text("+1") }
                    TextButton(onClick = { vm.nudgeSelectedOffset(dy = 10f) }) { Text("+10") }
                }
                Slider(value = t.offsetY, onValueChange = { vm.updateTransform { tr -> tr.copy(offsetY = it) } }, valueRange = -800f..800f)
            }
            "Scale" -> {
                Text("Scale ${"%.2f".format(t.scale)}", style = MaterialTheme.typography.labelMedium)
                Slider(value = t.scale, onValueChange = { vm.updateTransform { tr -> tr.copy(scale = it) } }, valueRange = 0.5f..2.0f)
            }
            "Rotate" -> {
                Text("Rotation ${"%.2f".format(t.rotation)}", style = MaterialTheme.typography.labelMedium)
                Slider(value = t.rotation, onValueChange = { vm.updateTransform { tr -> tr.copy(rotation = it) } }, valueRange = -5f..5f)
            }
            else -> {
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    ExportPreset.entries.forEach { p ->
                        Row { RadioButton(selected = s.preset == p, onClick = { vm.setPreset(p) }); Text(p.label) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0, 8, 16, 32).forEach { AssistChip(onClick = { vm.setFeather(it) }, label = { Text("Feather $it") }) }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { vm.resetSelectedTransform() }) { Text("Reset Selected") }
            TextButton(onClick = { vm.copySelectedTransformToAll() }) { Text("Copy to All") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { vm.refreshPreview() }) { Text("Refresh") }
            Button(onClick = { vm.export() }, enabled = !s.exporting, colors = ButtonDefaults.buttonColors(), modifier = Modifier.weight(1f)) {
                Text("Export")
            }
        }
    }
}

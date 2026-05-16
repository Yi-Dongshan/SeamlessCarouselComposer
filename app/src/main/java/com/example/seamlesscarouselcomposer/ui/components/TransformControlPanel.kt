package com.example.seamlesscarouselcomposer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.seamlesscarouselcomposer.data.ProjectViewModel

@Composable
fun TransformControlPanel(vm: ProjectViewModel) {
    val s = vm.state.value
    val t = s.images.getOrNull(s.selectedIndex)?.transform ?: return
    Text("offsetX: ${"%.1f".format(t.offsetX)}")
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        TextButton(onClick = { vm.nudgeSelectedOffset(dx = -10f) }) { Text("-10") }
        TextButton(onClick = { vm.nudgeSelectedOffset(dx = -1f) }) { Text("-1") }
        TextButton(onClick = { vm.nudgeSelectedOffset(dx = 1f) }) { Text("+1") }
        TextButton(onClick = { vm.nudgeSelectedOffset(dx = 10f) }) { Text("+10") }
    }
    Slider(value = t.offsetX, onValueChange = { vm.updateTransform { t -> t.copy(offsetX = it) } }, valueRange = -800f..800f)
    Text("offsetY: ${"%.1f".format(t.offsetY)}")
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        TextButton(onClick = { vm.nudgeSelectedOffset(dy = -10f) }) { Text("-10") }
        TextButton(onClick = { vm.nudgeSelectedOffset(dy = -1f) }) { Text("-1") }
        TextButton(onClick = { vm.nudgeSelectedOffset(dy = 1f) }) { Text("+1") }
        TextButton(onClick = { vm.nudgeSelectedOffset(dy = 10f) }) { Text("+10") }
    }
    Slider(value = t.offsetY, onValueChange = { vm.updateTransform { t -> t.copy(offsetY = it) } }, valueRange = -800f..800f)
    Text("scale: ${"%.2f".format(t.scale)}")
    Slider(value = t.scale, onValueChange = { vm.updateTransform { t -> t.copy(scale = it) } }, valueRange = 0.5f..2.0f)
    Text("rotation: ${"%.2f".format(t.rotation)}")
    Slider(value = t.rotation, onValueChange = { vm.updateTransform { t -> t.copy(rotation = it) } }, valueRange = -5f..5f)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TextButton(onClick = { vm.resetSelectedTransform() }) { Text("重置当前") }
        TextButton(onClick = { vm.copySelectedTransformToAll() }) { Text("复制到全部") }
    }
    // TODO: gesture transform (pinch/drag/rotate)
}

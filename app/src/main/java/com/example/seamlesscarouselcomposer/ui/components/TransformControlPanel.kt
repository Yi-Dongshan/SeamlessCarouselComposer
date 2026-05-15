package com.example.seamlesscarouselcomposer.ui.components

import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.seamlesscarouselcomposer.data.ProjectViewModel

@Composable
fun TransformControlPanel(vm: ProjectViewModel) {
    val s = vm.state.value
    val t = s.images.getOrNull(s.selectedIndex)?.transform ?: return
    Text("offsetX: ${"%.1f".format(t.offsetX)}")
    Slider(value = t.offsetX, onValueChange = { vm.updateTransform { t -> t.copy(offsetX = it) } }, valueRange = -800f..800f)
    Text("offsetY: ${"%.1f".format(t.offsetY)}")
    Slider(value = t.offsetY, onValueChange = { vm.updateTransform { t -> t.copy(offsetY = it) } }, valueRange = -800f..800f)
    Text("scale: ${"%.2f".format(t.scale)}")
    Slider(value = t.scale, onValueChange = { vm.updateTransform { t -> t.copy(scale = it) } }, valueRange = 0.5f..2.0f)
    Text("rotation: ${"%.2f".format(t.rotation)}")
    Slider(value = t.rotation, onValueChange = { vm.updateTransform { t -> t.copy(rotation = it) } }, valueRange = -5f..5f)
    // TODO: gesture transform (pinch/drag/rotate)
}

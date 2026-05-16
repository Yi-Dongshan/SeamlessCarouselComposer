package com.example.seamlesscarouselcomposer.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.seamlesscarouselcomposer.data.ProjectViewModel

@Composable
fun HomeScreen(vm: ProjectViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) {
        if (it.size in 2..10) vm.setImages(it)
    }
    Surface(Modifier.fillMaxSize()) {
        if (state.images.isEmpty()) {
            Column(Modifier.padding(24.dp)) {
                Text("SeamlessCarouselComposer", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(12.dp))
                Text("导入 2-10 张同背景照片，拼接导出连续轮播图")
                Spacer(Modifier.height(20.dp))
                Button(onClick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) { Text("选择照片") }
            }
        } else {
            EditorScreen(vm)
        }
    }
}

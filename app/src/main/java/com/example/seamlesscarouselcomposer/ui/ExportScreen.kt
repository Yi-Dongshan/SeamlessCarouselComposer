package com.example.seamlesscarouselcomposer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.seamlesscarouselcomposer.model.ExportResult

@Composable
fun ExportScreen(result: ExportResult) {
    Column {
        Text("导出成功")
        Text("Full: ${result.fullUri}")
        result.pageUris.forEachIndexed { i, uri -> Text("Page ${i + 1}: $uri") }
    }
}

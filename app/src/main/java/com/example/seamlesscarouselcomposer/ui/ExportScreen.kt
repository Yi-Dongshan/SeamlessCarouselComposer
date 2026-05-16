package com.example.seamlesscarouselcomposer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.seamlesscarouselcomposer.model.ExportResult

@Composable
fun ExportScreen(result: ExportResult) {
    Column {
        Text("导出成功：已保存到 Pictures/SeamlessCarouselComposer")
        Text("共 ${result.pageUris.size + 1} 张图片（1 张总图 + ${result.pageUris.size} 张分页图）")
        Text("分页数量：${result.pageUris.size}")
        Text("TODO: 分享按钮")
    }
}

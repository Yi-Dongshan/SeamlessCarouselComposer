package com.example.seamlesscarouselcomposer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.seamlesscarouselcomposer.model.ExportResult
import android.content.Intent

@Composable
fun ExportScreen(result: ExportResult) {
    val context = LocalContext.current
    Column {
        Text("导出成功：已保存到 Pictures/SeamlessCarouselComposer")
        Text("共 ${result.pageUris.size + 1} 张图片（1 张总图 + ${result.pageUris.size} 张分页图）")
        Text("分页数量：${result.pageUris.size}")
        Row {
            OutlinedButton(onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/jpeg"
                    putExtra(Intent.EXTRA_STREAM, result.fullUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "分享总图"))
            }) { Text("分享总图") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = {
                val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                    type = "image/jpeg"
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(result.pageUris))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "分享分页图"))
            }) { Text("分享分页图") }
        }
    }
}

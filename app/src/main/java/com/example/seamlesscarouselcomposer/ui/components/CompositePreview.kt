package com.example.seamlesscarouselcomposer.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp

@Composable
fun CompositePreview(bitmap: Bitmap, pageCount: Int, modifier: Modifier = Modifier) {
    val safePageCount = pageCount.coerceAtLeast(1)
    Canvas(
        modifier
            .fillMaxWidth()
            .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
    ) {
            val canvasHeight = size.height
            drawImage(bitmap.asImageBitmap(), dstSize = IntSize(size.width.toInt(), canvasHeight.toInt()))

            val segmentWidth = size.width / safePageCount
            for (i in 1 until safePageCount) {
                val x = i * segmentWidth
                drawLine(Color.White, Offset(x, 0f), Offset(x, canvasHeight), strokeWidth = 2f)
                drawLine(Color.Black.copy(alpha = 0.5f), Offset(x + 1f, 0f), Offset(x + 1f, canvasHeight), strokeWidth = 1f)
            }

            drawIntoCanvas { c ->
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 28.sp.toPx()
                    isFakeBoldText = true
                    setShadowLayer(8f, 0f, 0f, android.graphics.Color.BLACK)
                }
                val textY = 44.sp.toPx()
                repeat(safePageCount) { idx ->
                    val x = idx * segmentWidth + 16f
                    c.nativeCanvas.drawText("Page ${idx + 1}", x, textY, paint)
                }
            }
    }
}

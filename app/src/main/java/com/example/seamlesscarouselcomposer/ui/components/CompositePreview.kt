package com.example.seamlesscarouselcomposer.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import kotlin.math.max

@Composable
fun CompositePreview(
    bitmap: Bitmap,
    pageCount: Int,
    modifier: Modifier = Modifier,
    onTransformGesture: ((pan: Offset, zoom: Float, rotation: Float) -> Unit)? = null
) {
    val safePageCount = pageCount.coerceAtLeast(1)
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val viewportWidthPx = with(density) { maxWidth.toPx() }
        val viewportHeightPx = with(density) { maxHeight.toPx() }
        val fittedHeightPx = viewportHeightPx.coerceAtLeast(1f)
        val fittedWidthPx = (fittedHeightPx * bitmap.width.toFloat() / bitmap.height.toFloat()).coerceAtLeast(1f)
        val drawWidthPx = max(fittedWidthPx, viewportWidthPx)
        val drawWidthDp = with(density) { drawWidthPx.toDp() }
        val drawHeightDp = with(density) { fittedHeightPx.toDp() }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .height(drawHeightDp)
            ) {
                Canvas(
                    modifier = Modifier
                        .width(drawWidthDp)
                        .fillMaxHeight()
                        .pointerInput(onTransformGesture) {
                            if (onTransformGesture != null) {
                                detectTransformGestures { _, pan, zoom, rotation ->
                                    onTransformGesture(pan, zoom, rotation)
                                }
                            }
                        }
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
        }
    }
}

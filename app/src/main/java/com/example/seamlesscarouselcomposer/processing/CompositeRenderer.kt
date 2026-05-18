package com.example.seamlesscarouselcomposer.processing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import com.example.seamlesscarouselcomposer.model.CompositeProject

class CompositeRenderer {
    fun renderPreviewProject(context: Context, project: CompositeProject, maxWidth: Int = 1600): Bitmap {
        val full = renderFullComposite(context, project)
        return renderPreviewComposite(full, maxWidth)
    }

    fun renderFullComposite(context: Context, project: CompositeProject): Bitmap {
        val pageCount = project.images.size
        val width = project.preset.pageWidth * pageCount
        val height = project.preset.pageHeight
        val out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        project.images.forEachIndexed { i, source ->
            val src = BitmapLoader.load(context.contentResolver, source.uri)
            val base = ImageTransformUtils.coverMatrix(src.width, src.height, width, height)
            val p = source.transform
            val extra = Matrix().apply {
                postTranslate(p.offsetX, p.offsetY)
                postScale(p.scale, p.scale, width / 2f, height / 2f)
                postRotate(p.rotation, width / 2f, height / 2f)
            }
            val matrix = Matrix(base).apply { postConcat(extra) }
            canvas.save()
            canvas.clipRect(i * project.preset.pageWidth, 0, (i + 1) * project.preset.pageWidth, height)
            canvas.drawBitmap(src, matrix, null)
            canvas.restore()
            // TODO: seam feather alpha blending based on project.featherPx
        }
        // TODO: OpenCV auto alignment (ORB + Homography + warpPerspective)
        return out
    }

    fun splitIntoPages(fullComposite: Bitmap, pageWidth: Int, pageHeight: Int, pageCount: Int): List<Bitmap> =
        (0 until pageCount).map { i -> Bitmap.createBitmap(fullComposite, i * pageWidth, 0, pageWidth, pageHeight) }

    fun renderPreviewComposite(fullComposite: Bitmap, maxWidth: Int = 1600): Bitmap {
        if (fullComposite.width <= maxWidth) return fullComposite
        val scale = maxWidth.toFloat() / fullComposite.width.toFloat()
        val targetHeight = (fullComposite.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(fullComposite, maxWidth, targetHeight, true)
    }
}

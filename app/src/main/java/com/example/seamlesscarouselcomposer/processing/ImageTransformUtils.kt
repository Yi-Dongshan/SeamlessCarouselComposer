package com.example.seamlesscarouselcomposer.processing

import android.graphics.Bitmap
import android.graphics.Matrix

object ImageTransformUtils {
    fun scaleCropBitmapToCanvas(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val scale = maxOf(targetWidth / bitmap.width.toFloat(), targetHeight / bitmap.height.toFloat())
        val scaledW = bitmap.width * scale
        val scaledH = bitmap.height * scale
        val dx = (targetWidth - scaledW) / 2f
        val dy = (targetHeight - scaledH) / 2f
        val matrix = Matrix().apply { postScale(scale, scale); postTranslate(dx, dy) }
        return Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888).also {
            android.graphics.Canvas(it).drawBitmap(bitmap, matrix, null)
        }
    }

    fun coverMatrix(srcW: Int, srcH: Int, targetW: Int, targetH: Int): Matrix {
        val scale = maxOf(targetW / srcW.toFloat(), targetH / srcH.toFloat())
        return Matrix().apply {
            postScale(scale, scale)
            postTranslate((targetW - srcW * scale) / 2f, (targetH - srcH * scale) / 2f)
        }
    }
}

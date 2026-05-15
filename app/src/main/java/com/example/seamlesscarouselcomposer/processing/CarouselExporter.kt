package com.example.seamlesscarouselcomposer.processing

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.seamlesscarouselcomposer.model.ExportResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CarouselExporter {
    fun export(context: Context, full: Bitmap, pages: List<Bitmap>): ExportResult {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val base = "SCC_${stamp}"
        val fullUri = saveJpeg(context, full, "${base}_full.jpg")
        val pageUris = pages.mapIndexed { idx, b -> saveJpeg(context, b, "${base}_page_${(idx + 1).toString().padStart(2, '0')}.jpg") }
        return ExportResult(fullUri, pageUris)
    }

    private fun saveJpeg(context: Context, bitmap: Bitmap, name: String): android.net.Uri {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/SeamlessCarouselComposer")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        context.contentResolver.openOutputStream(uri)?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear(); values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, values, null, null)
        }
        return uri
    }
}

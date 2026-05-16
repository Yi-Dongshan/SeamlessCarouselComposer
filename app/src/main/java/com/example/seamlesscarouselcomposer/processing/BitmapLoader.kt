package com.example.seamlesscarouselcomposer.processing

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore

object BitmapLoader {
    fun load(contentResolver: ContentResolver, uri: android.net.Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
        } else {
            @Suppress("DEPRECATION") MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }
}

package com.example.seamlesscarouselcomposer.model

import android.net.Uri

data class SourceImage(
    val id: String,
    val uri: Uri,
    val displayName: String,
    val transform: TransformParams = TransformParams()
)

data class TransformParams(
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val scale: Float = 1f,
    val rotation: Float = 0f
)

enum class ExportPreset(val label: String, val pageWidth: Int, val pageHeight: Int) {
    XHS_3_4("小红书 3:4", 1080, 1440),
    SQUARE_1_1("方图 1:1", 1080, 1080),
    VERTICAL_9_16("竖屏 9:16", 1080, 1920)
}

data class CompositeProject(
    val images: List<SourceImage>,
    val preset: ExportPreset,
    val featherPx: Int = 0
)

data class ExportResult(val fullUri: Uri, val pageUris: List<Uri>)

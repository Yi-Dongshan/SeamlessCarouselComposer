package com.example.seamlesscarouselcomposer.data

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.seamlesscarouselcomposer.model.CompositeProject
import com.example.seamlesscarouselcomposer.model.ExportPreset
import com.example.seamlesscarouselcomposer.model.ExportResult
import com.example.seamlesscarouselcomposer.model.SourceImage
import com.example.seamlesscarouselcomposer.model.TransformParams
import com.example.seamlesscarouselcomposer.processing.CarouselExporter
import com.example.seamlesscarouselcomposer.processing.CompositeRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ProjectViewModel(app: Application) : AndroidViewModel(app) {
    data class UiState(
        val images: List<SourceImage> = emptyList(),
        val selectedIndex: Int = 0,
        val preset: ExportPreset = ExportPreset.XHS_3_4,
        val featherPx: Int = 0,
        val preview: Bitmap? = null,
        val previewRendering: Boolean = false,
        val exporting: Boolean = false,
        val exportResult: ExportResult? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
    private val renderer = CompositeRenderer()
    private val exporter = CarouselExporter()
    private var previewJob: Job? = null
    private var previewGeneration: Long = 0L

    fun setImages(uris: List<android.net.Uri>) {
        _state.value = _state.value.copy(
            images = uris.mapIndexed { i, uri -> SourceImage(UUID.randomUUID().toString(), uri, "Photo ${i + 1}") },
            selectedIndex = 0
        )
        schedulePreviewRefresh()
    }

    fun selectImage(idx: Int) {
        _state.value = _state.value.copy(selectedIndex = idx)
    }

    fun setPreset(preset: ExportPreset) {
        _state.value = _state.value.copy(preset = preset)
        schedulePreviewRefresh()
    }

    fun setFeather(px: Int) {
        _state.value = _state.value.copy(featherPx = px)
        schedulePreviewRefresh()
    }

    fun resetSelectedTransform() {
        updateTransform { TransformParams() }
    }

    fun copySelectedTransformToAll() {
        val s = _state.value
        val selected = s.images.getOrNull(s.selectedIndex)?.transform ?: return
        _state.value = s.copy(images = s.images.map { it.copy(transform = selected) })
        schedulePreviewRefresh()
    }

    fun nudgeSelectedOffset(dx: Float = 0f, dy: Float = 0f) {
        updateTransform { t -> t.copy(offsetX = t.offsetX + dx, offsetY = t.offsetY + dy) }
    }

    fun commitPreviewRefresh() {
        schedulePreviewRefresh()
    }

    fun updateTransform(update: (TransformParams) -> TransformParams, refreshPreview: Boolean = true) {
        val s = _state.value
        if (s.images.isEmpty()) return
        val list = s.images.toMutableList()
        val i = s.selectedIndex
        list[i] = list[i].copy(transform = update(list[i].transform))
        _state.value = s.copy(images = list)
        if (refreshPreview) schedulePreviewRefresh()
    }

    private fun schedulePreviewRefresh() {
        val generation = ++previewGeneration
        previewJob?.cancel()
        previewJob = viewModelScope.launch {
            delay(180)
            val snapshot = _state.value
            if (snapshot.images.isEmpty()) return@launch

            _state.value = _state.value.copy(previewRendering = true)
            try {
                val preview = withContext(Dispatchers.IO) {
                    renderer.renderPreviewProject(
                        getApplication(),
                        CompositeProject(snapshot.images, snapshot.preset, snapshot.featherPx)
                    )
                }
                if (isActive && generation == previewGeneration) {
                    _state.value = _state.value.copy(preview = preview)
                }
            } finally {
                if (isActive && generation == previewGeneration) {
                    _state.value = _state.value.copy(previewRendering = false)
                }
            }
        }
    }

    fun export() = viewModelScope.launch(Dispatchers.IO) {
        val s = _state.value
        if (s.images.isEmpty()) return@launch
        _state.value = _state.value.copy(exporting = true)
        val full = renderer.renderFullComposite(getApplication(), CompositeProject(s.images, s.preset, s.featherPx))
        val pages = renderer.splitIntoPages(full, s.preset.pageWidth, s.preset.pageHeight, s.images.size)
        val result = exporter.export(getApplication(), full, pages)
        val preview = renderer.renderPreviewComposite(full)
        _state.value = _state.value.copy(exporting = false, exportResult = result, preview = preview)
    }
}

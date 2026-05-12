package com.essay.corrector.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.essay.corrector.data.model.CorrectionResult
import com.essay.corrector.data.repository.EssayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EssayUiState(
    val isLoading: Boolean = false,
    val selectedImageUri: Uri? = null,
    val result: CorrectionResult? = null,
    val error: String? = null,
    val currentPage: Page = Page.UPLOAD,
)

enum class Page {
    UPLOAD,
    RESULT,
}

class EssayViewModel : ViewModel() {

    private val repository = EssayRepository()

    private val _uiState = MutableStateFlow(EssayUiState())
    val uiState: StateFlow<EssayUiState> = _uiState.asStateFlow()

    fun selectImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            selectedImageUri = uri,
            error = null,
        )
    }

    fun clearImage() {
        _uiState.value = _uiState.value.copy(
            selectedImageUri = null,
            error = null,
        )
    }

    fun uploadEssay(imagePath: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = repository.uploadEssay(imagePath)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    result = result,
                    currentPage = Page.RESULT,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "上传失败",
                )
            }
        }
    }

    fun goBack() {
        _uiState.value = _uiState.value.copy(
            currentPage = Page.UPLOAD,
            result = null,
            error = null,
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

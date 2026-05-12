package com.essay.corrector

import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.essay.corrector.ui.screen.ResultScreen
import com.essay.corrector.ui.screen.UploadScreen
import com.essay.corrector.ui.theme.EssayCorrectorTheme
import com.essay.corrector.viewmodel.EssayViewModel
import com.essay.corrector.viewmodel.Page
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EssayCorrectorTheme {
                MainContent()
            }
        }
    }

    @Composable
    private fun MainContent() {
        val viewModel: EssayViewModel = viewModel()
        val uiState by viewModel.uiState.collectAsState()

        when (uiState.currentPage) {
            Page.UPLOAD -> {
                UploadScreen(
                    isLoading = uiState.isLoading,
                    selectedImageUri = uiState.selectedImageUri,
                    error = uiState.error,
                    onImageSelected = viewModel::selectImage,
                    onClearImage = viewModel::clearImage,
                    onUpload = { uri ->
                        val path = getPathFromUri(uri)
                        if (path != null) {
                            viewModel.uploadEssay(path)
                        }
                    },
                    onClearError = viewModel::clearError,
                )

                var lastBackTime by remember { mutableLongStateOf(0L) }

                BackHandler(enabled = true) {
                    val now = System.currentTimeMillis()
                    if (now - lastBackTime < 2000) {
                        finish()
                    } else {
                        lastBackTime = now
                        Toast.makeText(this@MainActivity, "再按一次退出应用", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Page.RESULT -> {
                ResultScreen(
                    result = uiState.result ?: return,
                    onBack = viewModel::goBack,
                )

                BackHandler(enabled = true) {
                    viewModel.goBack()
                }
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload_", ".jpg", cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            inputStream.close()
            tempFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}

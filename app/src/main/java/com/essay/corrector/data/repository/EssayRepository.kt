package com.essay.corrector.data.repository

import com.essay.corrector.data.model.CorrectionResult
import com.essay.corrector.data.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EssayRepository {

    private val api = RetrofitClient.essayApi

    suspend fun uploadEssay(imagePath: String): CorrectionResult {
        val file = File(imagePath)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestBody,
        )
        return api.uploadEssay(multipartBody)
    }
}

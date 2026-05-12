package com.essay.corrector.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CorrectionResult(
    val qrCodeInfo: QRCodeInfo = QRCodeInfo(),
    val originalText: String = "",
    val errors: List<CorrectionError> = emptyList(),
)

@Serializable
data class QRCodeInfo(
    val courseId: String = "",
    val classId: String = "",
    val scheduleId: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val gender: String = "",
)

@Serializable
data class CorrectionError(
    val original: String = "",
    val corrected: String = "",
    val explanation: String = "",
)

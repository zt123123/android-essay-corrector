package com.essay.corrector.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.essay.corrector.data.model.CorrectionError
import com.essay.corrector.data.model.CorrectionResult
import com.essay.corrector.data.model.QRCodeInfo
import com.essay.corrector.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    result: CorrectionResult,
    onBack: () -> Unit,
) {
    val errorCount = result.errors.size
    val score = maxOf(0, 100 - errorCount * 5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("批改结果", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = result.qrCodeInfo.studentName.ifEmpty { "未知学生" },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            StudentInfoCard(qrCodeInfo = result.qrCodeInfo)

            Spacer(modifier = Modifier.height(12.dp))

            OriginalTextCard(text = result.originalText)

            Spacer(modifier = Modifier.height(12.dp))

            ScoreCard(score = score, errorCount = errorCount)

            Spacer(modifier = Modifier.height(12.dp))

            if (result.errors.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = Green100),
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Green500,
                            modifier = Modifier.size(40.dp),
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "未发现语法错误，作文很棒！",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = Green700,
                        )
                    }
                }
            } else {
                result.errors.forEachIndexed { index, error ->
                    ErrorCard(index = index + 1, error = error)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StudentInfoCard(qrCodeInfo: QRCodeInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👤", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "学生信息",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            InfoGrid(qrCodeInfo = qrCodeInfo)
        }
    }
}

@Composable
private fun InfoGrid(qrCodeInfo: QRCodeInfo) {
    val items = listOf(
        "姓名" to qrCodeInfo.studentName,
        "学号" to qrCodeInfo.studentId,
        "性别" to qrCodeInfo.gender,
        "课程" to qrCodeInfo.courseId,
        "班级" to qrCodeInfo.classId,
        "排课" to qrCodeInfo.scheduleId,
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                row.forEach { (label, value) ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                        )
                        Text(
                            text = value.ifEmpty { "-" },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun OriginalTextCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📄", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "OCR 识别原文",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Text(
                    text = text.ifEmpty { "未识别到内容" },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4,
                )
            }
        }
    }
}

@Composable
private fun ScoreCard(score: Int, errorCount: Int) {
    val scoreColor = when {
        score >= 80 -> Green500
        score >= 60 -> Yellow500
        else -> Red500
    }
    val bgColor = when {
        score >= 80 -> Green100
        score >= 60 -> Color(0xFFFFFBE6)
        else -> Red100
    }
    val label = when {
        score >= 90 -> "优秀"
        score >= 80 -> "良好"
        score >= 60 -> "及格"
        else -> "需要加强"
    }
    val desc = when {
        score >= 90 -> "作文整体质量很高"
        score >= 80 -> "有一些语法问题需要注意"
        score >= 60 -> "存在较多语法问题需要修正"
        else -> "建议认真复习语法规则"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = bgColor),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$score",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = scoreColor,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "综合评分 · $label",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "发现 $errorCount 处语法问题",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(index: Int, error: CorrectionError) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Default.Error,
                    contentDescription = null,
                    tint = Yellow500,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "问题 $index",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                DiffRow(type = "原文", text = error.original, isOriginal = true)
                Spacer(modifier = Modifier.height(8.dp))
                DiffRow(type = "修正", text = error.corrected, isOriginal = false)

                if (error.explanation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = Blue100,
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Icon(
                                Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = Blue500,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = error.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = Blue700,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiffRow(type: String, text: String, isOriginal: Boolean) {
    val bgColor = if (isOriginal) Red100 else Green100
    val textColor = if (isOriginal) Red700 else Green700
    val labelBg = if (isOriginal) Red500 else Green500
    val labelColor = Color.White

    Row(verticalAlignment = Alignment.Top) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = labelBg,
        ) {
            Text(
                text = type,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = labelColor,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = MaterialTheme.shapes.small,
            color = bgColor,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
            )
        }
    }
}

# Android Essay Corrector

英文作文 AI 批改 Android 客户端，基于 Jetpack Compose 构建。拍照或选择答题纸图片，自动识别二维码、提取手写文字、AI 批改语法错误。

## 功能特性

- **拍照/相册上传** — 支持相机拍照和相册选择答题纸图片
- **图片预览** — 上传前可预览和移除已选图片
- **二维码识别** — 自动识别答题纸上的学生信息二维码
- **OCR 文字提取** — 提取手写英文作文内容
- **AI 语法批改** — 识别语法错误并提供修正建议和中文解释
- **评分展示** — 综合评分 + 错误详情 + 原文/修正对比

## 应用截图流程

```
上传页面                    批改结果页面
┌──────────────────┐     ┌──────────────────┐
│  📝 英文作文AI批改  │     │ ← 批改结果  张三   │
│                  │     │                  │
│  ┌────────────┐  │     │ 👤 学生信息       │
│  │  📂 选择图片  │  │     │ 姓名  张三       │
│  │  支持 JPG PNG │  │     │ 学号  101112     │
│  └────────────┘  │     │                  │
│                  │     │ 📄 OCR 识别原文    │
│  [拍照] [相册]    │     │ My favorite...   │
│                  │     │                  │
│  ①上传 ②识别 ③OCR ④AI │     │ 85 良好          │
└──────────────────┘     │ ┌──────────────┐ │
                         │ │ 原文 → 修正    │ │
                         │ └──────────────┘ │
                         └──────────────────┘
```

## 项目结构

```
app/src/main/java/com/essay/corrector/
├── MainActivity.kt                    # 单 Activity 入口
├── data/
│   ├── model/
│   │   └── Models.kt                  # 数据模型（CorrectionResult, QRCodeInfo, CorrectionError）
│   ├── network/
│   │   └── RetrofitClient.kt          # Retrofit 网络层（API 接口定义 + OkHttp 配置）
│   └── repository/
│       └── EssayRepository.kt         # 数据仓库（封装 API 调用）
├── ui/
│   ├── screen/
│   │   ├── UploadScreen.kt            # 上传页面（拍照/相册/预览/权限处理）
│   │   └── ResultScreen.kt            # 结果页面（学生信息/评分/错误对比）
│   └── theme/
│       ├── Color.kt                   # 颜色定义
│       ├── Shape.kt                   # 形状定义
│       ├── Theme.kt                   # Material3 主题
│       └── Type.kt                    # 字体排版
└── viewmodel/
    └── EssayViewModel.kt              # ViewModel（状态管理 + 协程网络请求）
```

## 技术栈

| 组件 | 技术 | 说明 |
|------|------|------|
| UI 框架 | Jetpack Compose + Material3 | 声明式 UI |
| 导航 | Navigation Compose | 页面导航 |
| 网络请求 | Retrofit + OkHttp | REST API 调用 |
| 序列化 | Kotlinx Serialization | JSON 解析 |
| 图片加载 | Coil Compose | 图片预览 |
| 状态管理 | ViewModel + StateFlow | 响应式状态 |

## 环境要求

- Android Studio Hedgehog+
- JDK 17
- Android SDK：minSdk 26，targetSdk 35，compileSdk 36
- 后端服务运行中（[essay-ai-corrector-api](https://github.com/zt123123/essay-ai-corrector-api)）

## 构建运行

1. 克隆项目并使用 Android Studio 打开

2. 修改后端地址（如需）

   编辑 `RetrofitClient.kt` 中的 `BASE_URL`：

   ```kotlin
   private const val BASE_URL = "http://your-server-ip:3001/"
   ```

3. 连接 Android 设备或启动模拟器，点击 Run

## API 接口

客户端调用后端 `POST /api/upload` 接口，Multipart 上传图片：

**请求**

```
POST /api/upload
Content-Type: multipart/form-data

file: <图片文件>
```

**响应**

```json
{
  "qrCodeInfo": {
    "courseId": "123",
    "classId": "456",
    "scheduleId": "789",
    "studentId": "101112",
    "studentName": "张三",
    "gender": "男"
  },
  "originalText": "My favorite animal is dog...",
  "errors": [
    {
      "original": "My favorite animal is dog.",
      "corrected": "My favorite animal is a dog.",
      "explanation": "需要在dog前加不定冠词a"
    }
  ]
}
```

## 权限说明

| 权限 | 用途 |
|------|------|
| `INTERNET` | 网络请求后端 API |
| `CAMERA` | 拍照上传答题纸 |
| `READ_MEDIA_IMAGES` | Android 13+ 读取相册图片 |
| `READ_EXTERNAL_STORAGE` | Android 12 及以下读取相册 |

## 评分规则

- 基础分 100 分
- 每发现一处语法错误扣 5 分
- 最低 0 分

## License

ISC

package com.rsupport.saftest.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.documentfile.provider.DocumentFile
import com.rsupport.saftest.Application
import com.rsupport.saftest.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date

object Util {
    val logCollector = MutableStateFlow("")

    //일단 한가지 아이콘으로 통일
    suspend fun checkFileIconType(file: DocumentFile,encoding: Boolean): String {
        val fileName = file.name ?: ""
        val icon = when {
            fileName.endsWith(".txt", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".jpg", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".pdf", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".ppt", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".docx", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".png", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".mp3", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".mp4", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".xlsx", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".zip", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".html", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".css", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".js", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".jpg", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".gif", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".mpg", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".csv", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".pptx", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".svg", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".xml", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".json", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".avi", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".php", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".jar", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".xls", ignoreCase = true) -> R.drawable.file
            fileName.endsWith(".csv", ignoreCase = true) -> R.drawable.file
            else -> R.drawable.folder
        }
        return if(encoding) drawableToBase64String(icon) else ""
    }

    fun checkAttributes(file: DocumentFile): Int {
        var result = 0
        if (file.canWrite()) {
            result += 14
        }
        if (file.canRead()) {
            result += 1
        }
        return result
    }

    fun extractLastPathComponent(path: String): String {
        val lastIndexOfSlash = path.lastIndexOf('/')
        return if (lastIndexOfSlash >= 0) {
            path.substring(lastIndexOfSlash + 1)
        } else {
            path
        }
    }

    fun formatEpochTime(epochTime: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(epochTime)
        return sdf.format(date)
    }

    private suspend fun drawableToBase64String(resId: Int): String = withContext(Dispatchers.Default) {
        val bitmap = BitmapFactory.decodeResource(Application.instance.resources, resId)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
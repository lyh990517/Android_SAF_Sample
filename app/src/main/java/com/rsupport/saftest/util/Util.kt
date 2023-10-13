package com.rsupport.saftest.util

import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Date

object Util {
    val logCollector = MutableStateFlow("")
    fun checkFileIconType(file: DocumentFile): String {
        val fileName = file.name ?: ""
        return when {
            fileName.endsWith(".txt", ignoreCase = true) -> "Text Document"
            fileName.endsWith(".jpg", ignoreCase = true) -> "JPEG Image"
            fileName.endsWith(".pdf", ignoreCase = true) -> "PDF Document"
            fileName.endsWith(".ppt", ignoreCase = true) -> "PowerPoint Presentation"
            fileName.endsWith(".docx", ignoreCase = true) -> "Word Document"
            fileName.endsWith(".png", ignoreCase = true) -> "PNG Image"
            fileName.endsWith(".mp3", ignoreCase = true) -> "MP3 Audio"
            fileName.endsWith(".mp4", ignoreCase = true) -> "MP4 Video"
            fileName.endsWith(".xlsx", ignoreCase = true) -> "Excel Spreadsheet"
            fileName.endsWith(".zip", ignoreCase = true) -> "ZIP Archive"
            fileName.endsWith(".html", ignoreCase = true) -> "HTML Document"
            fileName.endsWith(".css", ignoreCase = true) -> "CSS Stylesheet"
            fileName.endsWith(".js", ignoreCase = true) -> "JavaScript File"
            fileName.endsWith(".jpg", ignoreCase = true) -> "JPEG Image"
            fileName.endsWith(".gif", ignoreCase = true) -> "GIF Image"
            fileName.endsWith(".mpg", ignoreCase = true) -> "MPEG Video"
            fileName.endsWith(".csv", ignoreCase = true) -> "CSV Data"
            fileName.endsWith(".pptx", ignoreCase = true) -> "PowerPoint Document"
            fileName.endsWith(".svg", ignoreCase = true) -> "SVG Image"
            fileName.endsWith(".xml", ignoreCase = true) -> "XML Document"
            fileName.endsWith(".json", ignoreCase = true) -> "JSON Data"
            fileName.endsWith(".avi", ignoreCase = true) -> "AVI Video"
            fileName.endsWith(".php", ignoreCase = true) -> "PHP Script"
            fileName.endsWith(".jar", ignoreCase = true) -> "Java Archive"
            fileName.endsWith(".xls", ignoreCase = true) -> "Old Excel Spreadsheet"
            fileName.endsWith(".csv", ignoreCase = true) -> "CSV Data"
            else -> "Folder"
        }
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
}
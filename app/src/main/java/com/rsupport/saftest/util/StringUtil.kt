package com.rsupport.saftest.util

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date

object StringUtil {
    fun isSameRoute(
        currentPath: String,
        path: String
    ) = extractLastPathComponent(currentPath) != extractLastPathComponent(path)

    fun extractLastPathComponent(path: String): String {
        val lastIndexOfSlash = path.lastIndexOf('/')
        return if (lastIndexOfSlash >= 0) {
            path.substring(lastIndexOfSlash + 1)
        } else {
            path
        }
    }
    fun removeLastPathComponent(path: String): String {
        val lastIndexOfSlash = path.lastIndexOf('/')

        if (lastIndexOfSlash >= 0) {
            return path.substring(0, lastIndexOfSlash)
        }

        return path
    }

    fun isFile(path: String): Boolean {
        val fileExtensions = listOf(".txt", ".jpg", ".png", ".pdf", ".doc", ".xls")

        val lastIndexOfDot = path.lastIndexOf('.')

        if (lastIndexOfDot >= 0) {
            val extension = path.substring(lastIndexOfDot)
            return extension in fileExtensions
        }

        return false
    }

    fun isUriInNextPath(uri: Uri, nextPath: String) =
        uri.path?.split("/")?.size == nextPath.split("/").size + 1

    fun formatEpochTime(epochTime: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(epochTime)
        return sdf.format(date)
    }
}
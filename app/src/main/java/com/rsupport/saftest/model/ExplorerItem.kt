package com.rsupport.saftest.model

import androidx.documentfile.provider.DocumentFile
import com.rsupport.saftest.util.StringUtil

data class ExplorerItem(
    val path: String,
    val displayName: String,
    val itemType: Int,
    val size: Long,
    val modifyDate: Long,
    val attribute: Int,
    val iconData: String,
    val subItems: List<ExplorerItem>
) {
    companion object {
        fun create(file: DocumentFile) =
            ExplorerItem(
                path = file.uri.path ?: "",
                displayName = file.uri.pathSegments.last(),
                attribute = -1, // ??
                iconData = "temp", // ??
                modifyDate = file.lastModified(),
                itemType = when {
                    file.isDirectory -> ItemType.Directory.value
                    file.isFile -> ItemType.File.value
                    else -> ItemType.Dot.value
                },
                size = file.length(),
                subItems = emptyList() // ??
            )

    }

    override fun toString(): String {
        return """
            ExplorerItem {
                path: $path,
                displayName: $displayName,
                attribute: $attribute,
                iconData: $iconData,
                modifyDate: epochTime ${StringUtil.formatEpochTime(modifyDate)},
                itemType: ${
            when (itemType) {
                1 -> ItemType.Directory
                2 -> ItemType.File
                else -> ItemType.Dot
            }
        },
                size: $size Byte,
                subItems: $subItems
            }
        """.trimIndent()
    }
}

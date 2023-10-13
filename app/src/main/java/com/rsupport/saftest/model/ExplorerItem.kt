package com.rsupport.saftest.model

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.rsupport.saftest.util.Util

data class ExplorerItem(
    val path: Uri,
    val displayName: String,
    val itemType: Int,
    val size: Long,
    val modifyDate: Long,
    val attribute: Int,
    val iconData: String,
    val subItems: MutableList<ExplorerItem>
) {
    companion object {
        suspend fun create(file: DocumentFile) =
            ExplorerItem(
                path = file.uri,
                displayName = file.uri.pathSegments.last(),
                attribute = Util.checkAttributes(file), // ??
                iconData = Util.checkFileIconType(file), // ??
                modifyDate = file.lastModified(),
                itemType = when {
                    file.isDirectory -> ItemType.Directory.value
                    file.isFile -> ItemType.File.value
                    else -> ItemType.Dot.value
                },
                size = file.length(),
                subItems = mutableListOf()
            )

    }

    override fun toString(): String {
        val subItemStrings = subItems.joinToString(",\n") { it.toString() }

        return """
    {
        "path": "$path",
        "displayName": "$displayName",
        "attribute": "$attribute",
        "iconData": "${iconData.take(200)}",
        "modifyDate": "epochTime ${Util.formatEpochTime(modifyDate)}",
        "itemType": ${
            when (itemType) {
                1 -> "\"${ItemType.Directory}\""
                2 -> "\"${ItemType.File}\""
                else -> "\"${ItemType.Dot}\""
            }
        },
        "size": "$size Byte",
        "subItems": [
            $subItemStrings
        ]
    }
    """.trimIndent()
    }
}

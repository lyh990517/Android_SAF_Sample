package com.rsupport.saftest.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.rsupport.saftest.Constants.HOME_DIR
import com.rsupport.saftest.util.StringUtil.isUriInNextPath
import java.io.File

object FileQueryUtil{
    //RC 방식
    fun getFilesInDirectory(filePath: String) = File(filePath).listFiles()!!.toList().map { it.toUri() }

    //ScopedStorage 사용
    fun getFilesInDirectoryByScopedStorage(context: Context, directoryPath: String): List<Uri> {
        val result = mutableListOf<Uri>()

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_MODIFIED
        )

        val selection = "${MediaStore.Files.FileColumns.DATA} like ?"
        val selectionArgs = arrayOf("$directoryPath%")

        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                if (columnIndex != -1) {
                    val filePath = cursor.getString(columnIndex)
                    val fileUri = Uri.parse("file://$filePath")
                    result.add(fileUri)
                }
            }
        }

        cursor?.close()
        return if (directoryPath == HOME_DIR) {
            result.filter { isUriInNextPath(it, HOME_DIR) }
        } else result.filter { it.path != directoryPath }
            .filter { isUriInNextPath(it, directoryPath) }
    }

    fun getFolderInfo(folderUri: Uri, context: Context) {
        val folder: DocumentFile? = DocumentFile.fromTreeUri(context, folderUri)

        if(folder != null && folder.isDirectory){
            val folderName: String = folder.name ?: ""

            val files: Array<DocumentFile> = folder.listFiles()
            Log.e("file", "${files.size}")
            for (file in files) {
                val fileName: String = file.name ?: ""
                val mimeType: String = file.type ?: ""
                val fileSize: Long = file.length()
                Log.e("file", "$fileName $fileSize $mimeType")
            }
        }

    }
}

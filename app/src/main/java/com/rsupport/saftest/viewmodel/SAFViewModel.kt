package com.rsupport.saftest.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import com.rsupport.saftest.state.SAFState
import com.rsupport.saftest.model.ExplorerItem
import com.rsupport.saftest.model.ItemType
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.FileNotFoundException

class SAFViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SAFState>(SAFState.Idle)
    val uiState = _uiState

    private val _fileList = MutableStateFlow<List<ExplorerItem>?>(null)

    fun selectFile() {
        _uiState.value = SAFState.OnSAFFile
    }

    fun selectFolder() {
        _uiState.value = SAFState.OnSAFFolder
    }

    fun setFileList(fileList: SnapshotStateList<ExplorerItem>) {
        _fileList.value = fileList.toList()
        _uiState.value = SAFState.Idle
    }

    fun sendFile(contentResolver: ContentResolver) {
        Log.e("fileSend", "Selected File Count: ${_fileList.value?.size}")
        try {
            _fileList.value?.forEachIndexed { index, file ->
                contentResolver.openInputStream(file.path).use { stream ->
                    var fileSend = 0
                    stream?.let {
                        Log.e("fileSend", "________________________________________")
                        Log.e("fileSend", "Start $index")
                        val buffer = ByteArray(1024 * 16)
                        while (true) {
                            if (file.itemType == ItemType.Directory.value){
                                Log.e("fileSend","is Directory")
                                break
                            }
                            val readSize = stream.read(buffer)
                            Log.e("fileSend", "${file.size} byte / $fileSend byte")
                            // writePacket
                            fileSend += readSize
                            if (readSize < 0) break
                        }
                        Log.e("fileSend", "Complete $index")
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFolderInfo(
        folderUri: Uri,
        context: Context,
        fileList: SnapshotStateList<ExplorerItem>,
        depth: Int
    ) {
        _uiState.value = SAFState.Loading
        try {
            val file: DocumentFile? = DocumentFile.fromTreeUri(context, folderUri)
            if (file != null && file.isDirectory) {
                Log.e("ExplorerItem", ExplorerItem.create(file).toString())
                val files: Array<DocumentFile> = file.listFiles()
                for (childFile in files) {
                    fileList.add(ExplorerItem.create(childFile))
                    getFolderInfo(childFile.uri, context, fileList, depth + 1)
                }
            }
            if (file != null && file.isFile) {
                fileList.add(ExplorerItem.create(file))
            }
            if (depth == 0) {
                _uiState.value = SAFState.Idle
            }
        } catch (e: RuntimeException) {
            getFileInfo(folderUri, context, fileList)
            e.printStackTrace()
        }
    }

    fun getFileInfo(
        folderUri: Uri,
        context: Context,
        fileList: SnapshotStateList<ExplorerItem>
    ) {
        _uiState.value = SAFState.Loading
        try {
            val file: DocumentFile? = DocumentFile.fromSingleUri(context, folderUri)
            if (file != null && file.isFile) {
                Log.e("ExplorerItem", ExplorerItem.create(file).toString())
                fileList.add(ExplorerItem.create(file))
            }
            _uiState.value = SAFState.Idle
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }
}
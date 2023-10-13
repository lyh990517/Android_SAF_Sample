package com.rsupport.saftest.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsupport.saftest.model.ExplorerItem
import com.rsupport.saftest.model.ItemType
import com.rsupport.saftest.state.SAFState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SAFViewModel : ViewModel() {

    //SAF 화면의 상태를 저장
    private val _uiState = MutableStateFlow<SAFState>(SAFState.Idle)
    val uiState = _uiState

    var fileList = mutableStateListOf<ExplorerItem>()
    val uploadSize = MutableStateFlow(0)
    val uploaded = MutableStateFlow(0)
    val uploadProgress = MutableStateFlow(0.0)
    val fileIndex = MutableStateFlow(0)

    private var job: Job? = null

    fun changeState(state: SAFState){
        _uiState.value = state
    }

    fun cancel(){
        job?.cancel()
        uploaded.value = 0
        uploadSize.value = 0
        uploadProgress.value = 0.0
        fileIndex.value = 0
    }

    fun showInfo() {
        fileList.forEach { Timber.tag("ExplorerItem").e(it.toString()) }
    }

    fun deleteFileList() {
        fileList.clear()
    }

    // 예제 함수
    fun sendFile(contentResolver: ContentResolver) {
        Timber.e("Selected File Count: " + fileList.size)
        job = viewModelScope.launch{
            try {
                uploaded.value = 0
                uploadSize.value = 0
                uploadProgress.value = 0.0
                fileIndex.value = 0
                uploadSize.value =
                    fileList.filter { it.itemType == ItemType.File.value }
                        .sumOf { it.size.toInt() }
                fileList.forEachIndexed { index, file ->
                    fileIndex.value = index + 1
                    contentResolver.openInputStream(file.path).use { stream ->
                        stream?.let {
                            Timber.e("________________________________________")
                            Timber.e("Start $index")
                            val buffer = ByteArray(1024 * 16)
                            while (true) {
                                delay(20)
                                uploadProgress.value =
                                    (uploaded.value.toDouble() / uploadSize.value) * 100
                                if (file.itemType == ItemType.Directory.value) {
                                    Timber.tag("fileSend").e("is Directory")
                                    break
                                }
                                val readSize = stream.read(buffer)
                                if (readSize < 0) break
                                // writePacket
                                uploaded.value += readSize
                            }
                            Timber.tag("fileSend").e("Complete %s", index)
                        }
                    }
                }
                fileIndex.value += 1 // 상태 변화를 통한 recompose 를 위함
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //최상위 폴더 바로 아래 요소만 보내도록함 depth 고정을 풀면 모든 파일을 보냄
    fun getFolderInfo(
        folderUri: Uri,
        context: Context,
        fileList: SnapshotStateList<ExplorerItem>, // ui에 보여줄 파일 목록
        depth: Int,
        parent: MutableList<ExplorerItem> = mutableListOf()
    ) {
        try {
            val file: DocumentFile? = DocumentFile.fromTreeUri(context, folderUri)
            when {
                file != null && file.isDirectory -> {
                    val files = file.listFiles().map { ExplorerItem.create(it) }
                    val folder = ExplorerItem.create(file)
                    if (depth == 1) fileList.add(folder)
                    files.forEachIndexed { _, childFile ->
                        if (depth == 1) folder.subItems.add(childFile)
                        if (depth > 1) parent.add(childFile)
                        getFolderInfo(
                            childFile.path,
                            context,
                            fileList,
                            depth + 1,
                            childFile.subItems
                        )
                    }
                }

                file != null && file.isFile -> {
                    if (depth == 1) fileList.add(ExplorerItem.create(file))
                }
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
        try {
            val file: DocumentFile? = DocumentFile.fromSingleUri(context, folderUri)
            if (file != null && file.isFile) {
                fileList.add(ExplorerItem.create(file))
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }
}
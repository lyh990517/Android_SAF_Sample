package com.rsupport.saftest.view

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rsupport.saftest.state.SAFState
import com.rsupport.saftest.model.ExplorerItem
import com.rsupport.saftest.model.Route

@Composable
fun SAFScreen(
    navHostController: NavHostController,
    uiState: State<SAFState>,
    getFileInfo: (Uri, Context, SnapshotStateList<ExplorerItem>) -> Unit,
    getFolderInfo: (Uri, Context, SnapshotStateList<ExplorerItem>) -> Unit,
    onFileSelect: () -> Unit,
    onFolderSelect: () -> Unit,
    onSet: (SnapshotStateList<ExplorerItem>) -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    onInfo: () -> Unit,
    uploadProgress: State<Double>,
    fileIndex: State<Int>,
    totalSize: State<Int>,
    uploaded: State<Int>,
) {
    val context = LocalContext.current
    val fileList = remember {
        mutableStateListOf<ExplorerItem>()
    }
    val isMultiple = rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        with(activityResult.data) {
            this?.clipData.let { data ->
                val size = data?.itemCount
                repeat(size ?: 0) { idx ->
                    data?.getItemAt(idx)?.uri?.let { uri ->
                        getFileInfo(uri, context, fileList)
                    }
                }

            }
            this?.data?.let { file ->
                getFolderInfo(file, context, fileList)
            }
        }
        onSet(fileList)
    }
    when (uiState.value) {
        SAFState.Idle -> {
            SAFContent(
                fileList,
                isMultiple,
                onFileSelect,
                onFolderSelect,
                onSend,
                onCancel,
                onInfo,
                uploadProgress,
                fileIndex,
                totalSize,
                uploaded,
                navHostController
            )
        }

        SAFState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        SAFState.OnSAFFile -> {
            fileList.clear()
            val intent = Intent(ACTION_OPEN_DOCUMENT)
            intent.type = "*/*"
            if (isMultiple.value) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            launcher.launch(intent)
        }

        SAFState.OnSAFFolder -> {
            fileList.clear()
            val intent = Intent(ACTION_OPEN_DOCUMENT_TREE)
            launcher.launch(intent)
        }
    }
    BackHandler {
        onCancel()
        navHostController.popBackStack()
    }
}

@Composable
private fun SAFContent(
    fileList: SnapshotStateList<ExplorerItem>,
    isMultiple: MutableState<Boolean>,
    onFileSelect: () -> Unit,
    onFolderSelect: () -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    onInfo: () -> Unit,
    uploadProgress: State<Double>,
    fileIndex: State<Int>,
    totalSize: State<Int>,
    uploaded: State<Int>,
    navHostController: NavHostController
) {
    val isSending = rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(modifier = Modifier.padding(10.dp), text = "Selected Files")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "파일 다중 선택")
                Checkbox(
                    onCheckedChange = { isMultiple.value = !isMultiple.value },
                    checked = isMultiple.value
                )
            }
        }
        Text(
            text = "uploadProgress : ${uploadProgress.value} %",
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "uploadSize : ${totalSize.value} / ${uploaded.value} byte",
            modifier = Modifier.padding(10.dp)
        )
        if (fileList.isNotEmpty()) {
            LazyColumn(Modifier.weight(6f)) {
                itemsIndexed(fileList.toList()) { index, file ->
                    FileItem(file, index, fileIndex) { explorerItem ->
                        fileList.remove(explorerItem)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(6f)
                    .fillMaxWidth()
            ) {
                Text(text = "Empty", Modifier.align(Alignment.Center))
            }
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier, onClick = onFileSelect
            ) {
                Text(text = "파일 선택")
            }
            Button(
                modifier = Modifier, onClick = onFolderSelect
            ) {
                Text(text = "폴더 전체 선택")
            }
            Button(
                modifier = Modifier, onClick = {
                    if (!isSending.value) onSend() else onCancel()
                    isSending.value = !isSending.value
                }
            ) {
                Text(text = if (!isSending.value) "파일 보내기" else "취소")
            }
            Button(
                modifier = Modifier, onClick = {
                    onInfo()
                    navHostController.navigate(Route.LOG)
                }
            ) {
                Text("로그 보기")
            }
        }
    }
}
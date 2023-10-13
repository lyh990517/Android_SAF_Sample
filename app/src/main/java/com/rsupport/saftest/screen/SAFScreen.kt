package com.rsupport.saftest.screen

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.model.ExplorerItem
import com.rsupport.saftest.state.SAFState
import com.rsupport.saftest.ui_component.ButtonMenuView
import com.rsupport.saftest.ui_component.FileListView
import com.rsupport.saftest.ui_component.StatusView
import com.rsupport.saftest.ui_component.dummyItem1
import com.rsupport.saftest.ui_component.dummyItem2

@Composable
fun SAFScreen(
    navHostController: NavHostController,
    uiState: State<SAFState>,
    getFileInfo: (Uri, Context, SnapshotStateList<ExplorerItem>) -> Unit,
    getFolderInfo: (Uri, Context, SnapshotStateList<ExplorerItem>) -> Unit,
    onChangeState: (SAFState) -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    onInfo: () -> Unit,
    fileList: SnapshotStateList<ExplorerItem>,
    uploadProgress: State<Double>,
    fileIndex: State<Int>,
    totalSize: State<Int>,
    uploaded: State<Int>,
) {
    val context = LocalContext.current
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
        onChangeState(SAFState.Idle)
    }
    when (uiState.value) {
        SAFState.Idle -> {
            SAFContent(
                fileList,
                isMultiple,
                onChangeState,
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
fun SAFContent(
    fileList: SnapshotStateList<ExplorerItem>,
    isMultiple: MutableState<Boolean>,
    onChangeState: (SAFState) -> Unit,
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
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        val modifier = Modifier.weight(1f)
        StatusView(Modifier,isMultiple, uploadProgress, totalSize, uploaded)
        FileListView(fileList, modifier, fileIndex)
        ButtonMenuView(
            onChangeState,
            isSending,
            onSend,
            onCancel,
            onInfo,
            navHostController
        )
    }
}

@Preview
@Composable
fun SAFScreenPreview() {
    SAFScreen(
        navHostController = rememberNavController(),
        uiState = remember { mutableStateOf(SAFState.Idle) },
        getFileInfo = { _, _, _ -> },
        getFolderInfo = { _, _, _ -> },
        onChangeState = {},
        onSend = { },
        onCancel = { },
        onInfo = { },
        fileList = remember { mutableStateListOf(dummyItem1, dummyItem2) },
        uploadProgress = remember { mutableStateOf(50.0) },
        fileIndex = remember { mutableStateOf(0) },
        totalSize = remember { mutableStateOf(100000) },
        uploaded = remember { mutableStateOf(50000) }
    )
}
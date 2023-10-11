package com.rsupport.saftest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.model.Route
import com.rsupport.saftest.view.SAFScreen
import com.rsupport.saftest.viewmodel.SAFViewModel

@Composable
fun FileExplorerApp(navHostController: NavHostController = rememberNavController()) {
    val viewModel = SAFViewModel()
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = Route.SAF_EXPLORER
    ) {

        composable(Route.SAF_EXPLORER) {
            SAFScreen(
                navHostController,
                viewModel.uiState.collectAsState(),
                getFileInfo = { uri, context, explorerItems -> viewModel.getFileInfo(uri, context, explorerItems) },
                getFolderInfo = { uri, context, explorerItems -> viewModel.getFolderInfo(uri, context, explorerItems, 0) },
                onFileSelect = { viewModel.selectFile() },
                onFolderSelect = { viewModel.selectFolder() },
                onSet = { viewModel.setFileList(it) },
                onSend = { viewModel.sendFile(context.contentResolver) },
                uploadProgress = viewModel.uploadProgress.collectAsState(),
                fileIndex = viewModel.fileIndex.collectAsState()
            )
        }
    }
}
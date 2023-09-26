package com.rsupport.saftest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.util.FileQueryUtil.getFilesInDirectory
import com.rsupport.saftest.util.FileQueryUtil.getFilesInDirectoryByScopedStorage
import com.rsupport.saftest.view.FileExplorerScreen
import com.rsupport.saftest.view.HomeScreen
import com.rsupport.saftest.view.PermissionRequestScreen
import com.rsupport.saftest.view.SAFScreen

@Composable
fun FileExplorerApp(navHostController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navHostController,
        startDestination = Route.HOME
    ) {
        composable(Route.HOME) {
            HomeScreen(navHostController)
        }
        composable(Route.REQUEST_PERMISSION) {
            PermissionRequestScreen(navHostController)
        }
        composable(Route.EXPLORER) {
            FileExplorerScreen(navHostController) { path, _ ->
                getFilesInDirectory(path)
            }
        }
        composable(Route.MEDIASTORE_EXPLORER) {
            FileExplorerScreen(navHostController) { path, context ->
                getFilesInDirectoryByScopedStorage(context, path)
            }
        }
        composable(Route.SAF_EXPLORER) {
            SAFScreen(navHostController)
        }
    }
}
package com.rsupport.saftest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.model.Route
import com.rsupport.saftest.screen.LogScreen
import com.rsupport.saftest.screen.SAFScreen
import com.rsupport.saftest.util.Util
import com.rsupport.saftest.viewmodel.SAFViewModel

@Composable
fun FileExplorerApp(navHostController: NavHostController = rememberNavController()) {
    val viewModel = SAFViewModel()
    val context = LocalContext.current

    NavHost(
        navController = navHostController,
        startDestination = Route.SAF_EXPLORER
    ) {
        composable(Route.LOG) {
            LogScreen(navHostController, log = Util.logCollector.collectAsState())
        }
        composable(Route.SAF_EXPLORER) {
            SAFScreen(
                navHostController,
                viewModel = viewModel
            )
        }
    }
}
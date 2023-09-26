package com.rsupport.saftest.view

import android.os.Environment
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.rsupport.saftest.Route
import java.io.File


@Composable
fun HomeScreen(navHostController: NavHostController) {
    val context = LocalContext.current
//    val externalDir = Environment.getExternalStorageDirectory()
//    val path = externalDir.path
//    val uri = FileProvider.getUriForFile(context, "com.yourapp.package.name.fileprovider", File(path))
//    Log.e("23","${uri.path}")
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navHostController.navigate(Route.EXPLORER) }) {
            Text(text = Route.EXPLORER)
        }
        Button(onClick = { navHostController.navigate(if (Environment.isExternalStorageManager()) Route.MEDIASTORE_EXPLORER else Route.REQUEST_PERMISSION) }) {
            Text(text = Route.MEDIASTORE_EXPLORER)
        }
        Button(onClick = { navHostController.navigate(Route.SAF_EXPLORER) }) {
            Text(text = Route.SAF_EXPLORER)
        }
        BackHandler {
            navHostController.popBackStack()
        }
    }
}
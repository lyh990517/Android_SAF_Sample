package com.rsupport.saftest.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rsupport.saftest.Constants.HOME_DIR
import com.rsupport.saftest.R
import com.rsupport.saftest.util.StringUtil.extractLastPathComponent
import com.rsupport.saftest.util.StringUtil.isFile
import com.rsupport.saftest.util.StringUtil.isSameRoute
import com.rsupport.saftest.util.StringUtil.removeLastPathComponent

@Composable
fun FileItem(file: Uri, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(file.path!!) },
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .padding(10.dp)
                    .size(width = 20.dp, height = 20.dp),
                painter = painterResource(id = R.drawable.folder),
                contentDescription = ""
            )
            Text(
                text = if (!isFile(file.path ?: "")) extractLastPathComponent(
                    file.path ?: ""
                ) else extractLastPathComponent(file.path ?: "").replace("/", ""),
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun FileExplorerScreen(
    navHostController: NavHostController,
    explore: (path: String, context: Context) -> List<Uri>
) {
    val context = LocalContext.current
    var currentPath by rememberSaveable { mutableStateOf(HOME_DIR) }
    val files = rememberSaveable { mutableStateOf(explore(currentPath, context)) }
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(6f)) {
            items(files.value) { file ->
                FileItem(file) { nextPath ->
                    if (isSameRoute(currentPath, nextPath) && !isFile(nextPath)) {
                        currentPath += extractLastPathComponent(nextPath)
                        files.value = explore(currentPath, context)
                        Log.e("path", extractLastPathComponent(currentPath))
                    }
                }
            }
        }
    }
    BackHandler {
        if (currentPath != HOME_DIR) {
            currentPath = removeLastPathComponent(currentPath)
            files.value = explore(currentPath, context)
            Log.e("path", currentPath)
        } else {
            navHostController.popBackStack()
        }
    }
}

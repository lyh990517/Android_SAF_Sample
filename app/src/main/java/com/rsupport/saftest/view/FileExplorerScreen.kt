package com.rsupport.saftest.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rsupport.saftest.util.Constants.HOME_DIR
import com.rsupport.saftest.R
import com.rsupport.saftest.model.ItemType
import com.rsupport.saftest.model.ExplorerItem
import com.rsupport.saftest.util.StringUtil
import com.rsupport.saftest.util.StringUtil.removeLastPathComponent

@Composable
fun FileItem(file: ExplorerItem, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(file.path) },
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(width = 20.dp, height = 20.dp),
                    painter = when (file.itemType) {
                        1 -> painterResource(id = R.drawable.folder)
                        2 -> painterResource(id = R.drawable.file)
                        else -> painterResource(id = R.drawable.file)
                    },
                    contentDescription = ""
                )
                Text(
                    text = file.displayName,
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            Column(Modifier.padding(8.dp)) {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(fontSize = 12.sp)
                ) {
                    Text(text = "modified: ${StringUtil.formatEpochTime(file.modifyDate)}")
                    Text(text = "size: ${file.size} byte")
                    Text(
                        text = "Type: ${
                            when (file.itemType) {
                                1 -> ItemType.Directory
                                2 -> ItemType.File
                                else -> ItemType.Dot
                            }
                        }"
                    )
                    Text(text = "attribute: ${file.attribute}")
                    Text(text = "iconData: ${file.iconData}")
                    Text(text = "path: ${file.path}")
                }
            }
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
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "no")
        LazyColumn(Modifier.weight(6f)) {
//            items(files.value) { file ->
//                FileItem(file) { nextPath ->
//                    if (isSameRoute(currentPath, nextPath) && !isFile(nextPath)) {
//                        currentPath += extractLastPathComponent(nextPath)
//                        files.value = explore(currentPath, context)
//                        Log.e("path", extractLastPathComponent(currentPath))
//                    }
//                }
//            }
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

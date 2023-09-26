package com.rsupport.saftest.view

import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.ACTION_OPEN_DOCUMENT_TREE
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rsupport.saftest.util.FileQueryUtil

@Composable
fun SAFScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val fileList = remember { mutableStateListOf<Uri?>() }
    val isMultiple = rememberSaveable { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        with(activityResult.data) {
            if (isMultiple.value) {
                this?.clipData.let { data ->
                    val size = data?.itemCount
                    repeat(size ?: 0) {
                        val file = data?.getItemAt(it)?.uri
                        fileList.add(file)
                        Log.e("file", "$file")
                    }
                }
            } else {
                this?.data?.let { file ->
                    fileList.add(file)
                    FileQueryUtil.getFolderInfo(file, context)
                    Log.e("file", "$file")
                }
            }
        }
    }
    Column(Modifier.fillMaxSize()) {
        Text(modifier = Modifier.padding(10.dp), text = "Selected Files")
        LazyColumn(Modifier.weight(1f)) {
            items(fileList.toList()) { file ->
                Log.e("file123", "$file")
                file?.let {
                    FileItem(file) {
                        Log.e("path", file.path ?: "")
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "multi select")
            Checkbox(
                onCheckedChange = { isMultiple.value = !isMultiple.value },
                checked = isMultiple.value
            )
            Button(modifier = Modifier.weight(1f), onClick = {
                fileList.clear()
                val intent = Intent(ACTION_OPEN_DOCUMENT)
                intent.type = "*/*"
                if (isMultiple.value) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                launcher.launch(intent)
            }) {
                Text(text = "open SAF Activity")
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            fileList.clear()
            val intent = Intent(ACTION_OPEN_DOCUMENT_TREE)
            launcher.launch(intent)
        }) {
            Text(text = "open SAF Activity for folder")
        }
    }
    BackHandler {
        navHostController.popBackStack()
    }
}
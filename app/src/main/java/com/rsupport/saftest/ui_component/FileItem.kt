package com.rsupport.saftest.ui_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rsupport.saftest.R
import com.rsupport.saftest.model.ExplorerItem
import com.rsupport.saftest.model.ItemType
import com.rsupport.saftest.util.Util

@Composable
fun FileItem(
    file: ExplorerItem,
    index: Int,
    currentFileIndex: State<Int>,
    onClick: (ExplorerItem) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(file) },
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Column(Modifier.wrapContentSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(width = 20.dp, height = 20.dp),
                    painter = when (file.itemType) {
                        ItemType.Directory.value -> painterResource(id = R.drawable.folder)
                        ItemType.File.value -> painterResource(id = R.drawable.file)
                        else -> painterResource(id = R.drawable.file)
                    },
                    contentDescription = ""
                )
                Text(
                    text = Util.extractLastPathComponent(file.displayName),
                    modifier = Modifier
                        .padding(10.dp)
                )
            }
            Column(Modifier.padding(8.dp)) {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(fontSize = 12.sp),
                ) {
                    Text(text = "modified: ${Util.formatEpochTime(file.modifyDate)}")
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

                    AnimatedVisibility(index == currentFileIndex.value - 1) {
                        Row {
                            Text(
                                text = "upload this...",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                            CircularProgressIndicator()
                        }
                    }
                    AnimatedVisibility(index <= currentFileIndex.value - 1) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun FileItemPreview() {
    val index = 0
    val currentFileIndex = remember { mutableStateOf(0) } // Assuming 1 is the current file index
    FileItem(
        file = dummyItem1,
        index = index,
        currentFileIndex = currentFileIndex
    ) { /* onClick function if needed */ }

}

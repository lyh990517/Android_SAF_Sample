package com.rsupport.saftest.ui_component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.rsupport.saftest.model.ExplorerItem

@Composable
fun FileListView(
    fileList: SnapshotStateList<ExplorerItem>,
    modifier: Modifier,
    fileIndex: State<Int>
) {
    if (fileList.isNotEmpty()) {
        LazyColumn(modifier) {
            itemsIndexed(fileList.toList()) { index, file ->
                FileItem(file, index, fileIndex) { explorerItem ->
                    fileList.remove(explorerItem)
                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(text = "Empty", Modifier.align(Alignment.Center))
        }
    }
}

@Composable
@Preview
fun FileListViewPreview() {
    val sampleFileList = remember { mutableStateListOf<ExplorerItem>() }
    sampleFileList.addAll(
        listOf(
            dummyItem1,
            dummyItem2
        )
    )

    val fileIndex = remember { mutableStateOf(0) }
    FileListView(
        fileList = sampleFileList,
        modifier = Modifier.background(Color.White),
        fileIndex = fileIndex
    )
}

val dummyItem1 = ExplorerItem(
    path = Uri.parse("content://com.android.externalstorage.documents/tree/primary:Download/Test/document/primary:Download/Test/Download/3gwegweg3"),
    displayName = "primary:Download/Test/Download/3gwegweg3",
    itemType = 1,
    size = 4096,
    modifyDate = 1663729939000,
    attribute = 15,
    iconData = "Folder Icon",
    subItems = mutableListOf()
)

val dummyItem2 = ExplorerItem(
    path = Uri.parse("content://com.android.externalstorage.documents/tree/primary:Download/Test/document/primary:Download/Test/test.jpg"),
    displayName = "primary:Download/Test/test.jpg",
    itemType = 2,
    size = 1043632,
    modifyDate = 1663815592000,
    attribute = 15,
    iconData = "JPEG Image Icon",
    subItems = mutableListOf()
)
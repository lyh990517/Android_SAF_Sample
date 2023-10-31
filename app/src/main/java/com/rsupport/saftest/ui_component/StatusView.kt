package com.rsupport.saftest.ui_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatusView(
    modifier: Modifier = Modifier,
    isMultiple: MutableState<Boolean>,
    uploadProgress: State<Double>,
    totalSize: State<Int>,
    uploaded: State<Int>
) {
    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(modifier = Modifier.padding(10.dp), text = "Selected Files")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "파일 다중 선택")
                Checkbox(
                    modifier = Modifier.testTag("file_option"),
                    onCheckedChange = { isMultiple.value = !isMultiple.value },
                    checked = isMultiple.value
                )
            }
        }
        Text(
            text = "uploadProgress : ${uploadProgress.value} %",
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "uploadSize : ${totalSize.value} / ${uploaded.value} byte",
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
@Preview
fun StatusViewPreview() {
    val isMultiple = remember { mutableStateOf(false) }
    val uploadProgress = remember { mutableStateOf(50.0) } // Sample upload progress
    val totalSize = remember { mutableStateOf(1024) } // Sample total size in bytes
    val uploaded = remember { mutableStateOf(512) } // Sample uploaded size in bytes
    StatusView(
        modifier = Modifier.background(Color.White),
        isMultiple = isMultiple,
        uploadProgress = uploadProgress,
        totalSize = totalSize,
        uploaded = uploaded
    )

}
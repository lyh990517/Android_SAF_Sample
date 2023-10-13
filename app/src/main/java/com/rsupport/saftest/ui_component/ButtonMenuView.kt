package com.rsupport.saftest.ui_component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.model.Route
import com.rsupport.saftest.state.SAFState

@Composable
fun ButtonMenuView(
    onChangeState: (SAFState) -> Unit,
    isSending: MutableState<Boolean>,
    onSend: () -> Unit,
    onCancel: () -> Unit,
    onInfo: () -> Unit,
    navHostController: NavHostController
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier, onClick = {
                onChangeState(SAFState.OnSAFFile)
            }
        ) {
            Text(text = "파일 선택")
        }
        Button(
            modifier = Modifier, onClick = {
                onChangeState(SAFState.OnSAFFolder)
            }
        ) {
            Text(text = "폴더 전체 선택")
        }
        Button(
            modifier = Modifier, onClick = {
                if (!isSending.value) onSend() else onCancel()
                isSending.value = !isSending.value
            }
        ) {
            Text(text = if (!isSending.value) "파일 보내기" else "취소")
        }
        Button(
            modifier = Modifier, onClick = {
                onInfo()
                navHostController.navigate(Route.LOG)
            }
        ) {
            Text("로그 보기")
        }
    }
}

@Composable
@Preview
fun ButtonMenuViewPreview() {
    ButtonMenuView(
        onChangeState = {},
        isSending = remember { mutableStateOf(false) },
        onSend = {},
        onCancel = {},
        onInfo = {},
        navHostController = rememberNavController()
    )
}
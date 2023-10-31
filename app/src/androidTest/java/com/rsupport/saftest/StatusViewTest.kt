package com.rsupport.saftest

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.screen.SAFScreen
import com.rsupport.saftest.ui_component.StatusView
import com.rsupport.saftest.viewmodel.SAFViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StatusViewTest {

    @get:Rule
    val composeRule = createComposeRule()
    private val safViewModel = SAFViewModel()

    @Before
    fun setUp() {
        composeRule.setContent {
            SAFScreen(navHostController = rememberNavController(), viewModel = safViewModel)
        }
        composeRule.onRoot().printToLog("for Test")
    }

    @Test
    fun `파일_선택_옵션을_클릭_한다`() {
        composeRule.onNodeWithTag("file_option").assertExists()
        composeRule.onNodeWithTag("file_option").performClick()
        composeRule.onNodeWithTag("file_option").assertIsOn()
    }

    @Test
    fun `텍스트가_표시_되는지_확인_한다`() {
        composeRule.onNodeWithText("Selected Files").assertExists()
        composeRule.onNodeWithText("uploadProgress : ${safViewModel.uploadProgress.value} %")
            .assertExists()
        composeRule.onNodeWithText("uploadSize : ${safViewModel.uploadSize.value} / ${safViewModel.uploaded.value} byte")
            .assertExists()
        composeRule.onNodeWithText("파일 다중 선택").assertExists()

    }
}
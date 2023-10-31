package com.rsupport.saftest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.screen.SAFScreen
import com.rsupport.saftest.ui_component.dummyItem1
import com.rsupport.saftest.util.Util
import com.rsupport.saftest.viewmodel.SAFViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ButtonViewTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val safViewModel = SAFViewModel()

    @Before
    fun setUp() {
        composeRule.setContent {
            SAFScreen(navHostController = rememberNavController(), viewModel = safViewModel)
        }
    }

    @Test
    fun `버튼이_표시되는지_확인한다`() {
        composeRule.onNodeWithText("파일 선택").assertIsDisplayed()
        composeRule.onNodeWithText("전체 삭제").assertIsDisplayed()
        composeRule.onNodeWithText("폴더 전체 선택").assertIsDisplayed()
        composeRule.onNodeWithTag("button_view").performScrollToNode(hasText("로그 보기"))
        composeRule.onNodeWithText("파일 보내기").assertIsDisplayed()
        composeRule.onNodeWithText("로그 보기").assertIsDisplayed()
    }

    @Test
    fun `파일보내기_버튼을_누른다`() {
        composeRule.onNodeWithText("파일 보내기").performClick()
        composeRule.onNodeWithText("취소").assertIsDisplayed()
    }

    @Test
    fun `전체_삭제_버튼을_누른다`() {
        safViewModel.fileList.add(dummyItem1)
        composeRule.onNodeWithText(
            Util.extractLastPathComponent(safViewModel.fileList[0].displayName),
            useUnmergedTree = true
        ).assertIsDisplayed()
        composeRule.onNodeWithText("전체 삭제").performClick()
        composeRule.onNodeWithTag("empty").assertIsDisplayed()
    }
}
package com.rsupport.saftest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.rememberNavController
import com.rsupport.saftest.screen.SAFScreen
import com.rsupport.saftest.ui_component.dummyItem1
import com.rsupport.saftest.ui_component.dummyItem2
import com.rsupport.saftest.util.Util
import com.rsupport.saftest.viewmodel.SAFViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FileListViewTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val safViewModel = SAFViewModel()
    @Before
    fun setUp(){
        composeRule.setContent {
            SAFScreen(navHostController = rememberNavController(), viewModel = safViewModel)
        }
    }

    @Test
    fun `파일리스트가_비어_있어야_한다`(){
        composeRule.onNodeWithTag("empty").assertIsDisplayed()
    }

    @Test
    fun `파일리스트에_파일을_넣는다`(){
        composeRule.onNodeWithTag("empty").assertIsDisplayed()
        safViewModel.fileList.add(dummyItem1)
        composeRule.onNodeWithTag("file_list").assertIsDisplayed()
        composeRule.onNodeWithTag("file_name",useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `파일을_클릭하면_삭제된다`(){
        safViewModel.fileList.add(dummyItem1)
        composeRule.onNodeWithTag("file_item").assertIsDisplayed()
        composeRule.onNodeWithTag("file_item").performClick()
        composeRule.onNodeWithTag("empty").assertIsDisplayed()
    }

    @Test
    fun `파일리스트를_스크롤한다`(){
        repeat(5){
            safViewModel.fileList.add(dummyItem1)
        }
        safViewModel.fileList.add(dummyItem2)
        composeRule.onNodeWithTag("file_list").assertExists()
        composeRule.onNodeWithTag("file_list").performScrollToIndex(4)
        Thread.sleep(1000)
        composeRule.onNodeWithText(Util.extractLastPathComponent(dummyItem2.displayName)).assertIsDisplayed()

    }
}
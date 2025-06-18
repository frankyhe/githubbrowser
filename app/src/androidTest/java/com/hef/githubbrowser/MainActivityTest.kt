package com.hef.githubbrowser

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hef.githubbrowser.ui.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/18
 */
class MainActivityTest {
    @get:Rule
    val composeTestRule = createComposeRule()

//    @Test
//    fun buttonClick_showsWelcomeMessage() {
//        // 设置测试内容
//        composeTestRule.setContent {
//            MinePrimaryPage()
//        }
//
//        // 查找节点并交互
//        composeTestRule
//            .onNodeWithText("Sign In")  // 匹配文本
//            .performClick()
//
//        // 验证结果
//        composeTestRule
//            .onNodeWithText("Welcome!")
//            .assertIsDisplayed()

    @Test
    fun testBottomNav(){
        val composeRule = createAndroidComposeRule<MainActivity>()

        composeRule.onNodeWithContentDescription("Mine Icon").performClick()
        composeRule.onNodeWithContentDescription("start search").assertIsNotDisplayed()

        composeRule.onNodeWithContentDescription("Search Icon").performClick()
        composeRule.onNodeWithContentDescription("start search").assertIsDisplayed()
    }

    @Test
    fun testStartSearchDialog(){
        val composeRule = createAndroidComposeRule<MainActivity>()

        composeRule.onNodeWithContentDescription("Mine Icon").performClick()
        composeRule.onNodeWithContentDescription("start search").performClick()

        composeRule.onNodeWithText("confirm").assertIsDisplayed()
    }


}
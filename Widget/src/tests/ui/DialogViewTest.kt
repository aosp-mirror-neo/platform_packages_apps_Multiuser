/*
 * Copyright (C) 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.multiuser.widget.ui.tests

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull

import android.content.Intent;

import com.android.multiuser.widget.data.ResourceRepository
import com.android.multiuser.widget.data.ResourceRepository.DialogViewModel
import com.android.multiuser.widget.ui.DialogView

@RunWith(AndroidJUnit4::class)
class DialogViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSwitchFailedDialog() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val resourceRepository = ResourceRepository(context.resources)
        val model = resourceRepository.getSwitchFailed()
        var dismissed = false
        var actionIntent: Intent? = null
        composeTestRule.setContent {
            DialogView(model, { dismissed = true }, { actionIntent = it })
        }
        val title = context.getString(R.string.widget_switch_failed_dialog_title)
        val message = context.getString(R.string.widget_switch_failed_dialog_message)
        val closeButton = context.getString(R.string.dialog_close_button_text)
        val actionButton = context.getString(R.string.widget_switch_disabled_dialog_open_settings_button)
        assertEquals(model.title, title)
        composeTestRule.onAllNodesWithText(title).assertCountEquals(1)
        assertEquals(model.message, message)
        composeTestRule.onAllNodesWithText(message).assertCountEquals(1)
        composeTestRule.onAllNodesWithText(closeButton).assertCountEquals(1)
        composeTestRule.onAllNodesWithText(actionButton).assertCountEquals(0)
        assertFalse(dismissed)
        composeTestRule.onNodeWithText(closeButton).performClick()
        assertTrue(dismissed)
    }

    @Test
    fun testSwitchNotAllowed() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val resourceRepository = ResourceRepository(context.resources)
        val model = resourceRepository.getSwitchDisabled()
        var dismissed = false
        var actionIntent: Intent? = null
        composeTestRule.setContent {
            DialogView(model, { dismissed = true }, { actionIntent = it })
        }
        val title: String = context.getString(R.string.widget_switch_disabled_dialog_title)
        val message: String = context.getString(R.string.widget_switch_disabled_dialog_message)
        val closeButton: String = context.getString(R.string.dialog_close_button_text)
        val actionButton: String = context.getString(R.string.widget_switch_disabled_dialog_open_settings_button)

        assertEquals(model.title, title)
        composeTestRule.onAllNodesWithText(title).assertCountEquals(1)
        assertEquals(model.message, message)
        composeTestRule.onAllNodesWithText(message).assertCountEquals(1)
        composeTestRule.onAllNodesWithText(closeButton).assertCountEquals(1)
        composeTestRule.onAllNodesWithText(actionButton).assertCountEquals(1)
        assertFalse(dismissed)
        composeTestRule.onNodeWithText(closeButton).performClick()
        assertTrue(dismissed)
        assertNull(actionIntent)
        composeTestRule.onNodeWithText(actionButton).performClick()
        assertNotNull(actionIntent)
    }
}

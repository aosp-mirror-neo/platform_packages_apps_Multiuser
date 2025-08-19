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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.provider.Settings

import androidx.test.platform.app.InstrumentationRegistry

import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.getActionIntent
import com.android.multiuser.widget.data.model.UserAddRestriction
import com.android.multiuser.widget.domain.AddUserUseCase
import com.android.multiuser.widget.domain.interactions.StartActivityUseCase
import com.android.multiuser.widget.R

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// --- Test Class for AddUserUseCase ---
@RunWith(JUnit4::class)
class AddUserUseCaseTest {

    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var intentRepository: IntentRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var addUserUseCase: AddUserUseCase

    private val testWidgetCategory = 0 // Arbitrary category

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        resources = context.resources
        intentRepository = IntentRepository(resources)
        userRepository = FakeUserRepository()
        addUserUseCase = AddUserUseCase(userRepository, intentRepository)
    }

    @Test
    fun testAddUserUseCaseReturnAddUserActivityIntentWhenNoRestrictions() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.NONE

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertNotNull("Intent should not be null", intent)

        assertEquals(ComponentName(IntentRepository.USER_DIALOGS_PACKAGE, IntentRepository.ADD_USER_ACTIVITY), intent.component)
        assertTrue("Intent flags should include NEW_TASK", intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
        assertTrue("Intent flags should include CLEAR_TASK", intent.flags and Intent.FLAG_ACTIVITY_CLEAR_TASK != 0)
    }

    // Helper to assert common properties of restricted dialog intents
    private fun assertRestrictedDialogIntentBase(intent: Intent) {
        assertEquals(ComponentName(IntentRepository.ERROR_DIALOGS_PACKAGE, IntentRepository.ERROR_DILOGS_ACTIVITY), intent.component)
        assertTrue("Intent flags should include NEW_TASK", intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
    }

    // Helper to assert the content of the dialog
    private fun assertDialogContent(intent: Intent, expectedTitleRes: Int, expectedMessageRes: Int, expectSettingsAction: Boolean = false) {
        val expectedTitle = resources.getString(expectedTitleRes)
        val expectedMessage = resources.getString(expectedMessageRes)

        assertEquals(expectedTitle, intent.getStringExtra(IntentRepository.TITLE))
        assertEquals(expectedMessage, intent.getStringExtra(IntentRepository.MESSAGE))

        val actionIntent = intent.getActionIntent()
        if (expectSettingsAction) {
            assertNotNull("Expected settings action intent", actionIntent)
            assertEquals(Settings.ACTION_USER_SETTINGS, actionIntent!!.action)
            assertEquals(ComponentName(IntentRepository.SETTINGS_PACKAGE, IntentRepository.SETTINGS_ACTIVITY), actionIntent.component)
            assertEquals(resources.getString(R.string.widget_dialog_open_settings_button), intent.getStringExtra(IntentRepository.ACTION_TEXT))
            val flags = intent.getIntExtra(IntentRepository.FLAGS, 0)
            assertTrue("Expected NEW_TASK flag in actionIntent", flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0)
            assertTrue("Expected CLEAR_TASK flag in actionIntent", flags and Intent.FLAG_ACTIVITY_CLEAR_TASK != 0)
        } else {
            assertNull("Unexpected action intent", actionIntent)
            assertNull("Unexpected action text", intent.getStringExtra(IntentRepository.ACTION_TEXT))
        }
    }

    @Test
    fun testAddUserUseCaseReturnWorkPolicyDialogWhenWorkPolicy() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.WORK_POLICY

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_switch_work_restriction_dialog_title,
            R.string.widget_add_work_restriction_dialog_message
        )
    }

    @Test
    fun testAddUserUseCaseReturnDisabledDialogWithSettingsActionWhenSwitchDisabled() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.SWITCH_DISABLED

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_add_failed_dialog_title,
            R.string.widget_switch_disabled_dialog_message,
            expectSettingsAction = true
        )
    }

    @Test
    fun testAddUserUseCaseReturnMaxLimitDialogWhenMaxUserLimit() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.MAX_USER_LIMIT

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_add_failed_dialog_title,
            R.string.widget_add_max_limit_dialog_message
        )
    }

    @Test
    fun testAddUserUseCaseReturnNotAdminDialogWhenNotAdmin() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.NOT_ADMIN_OR_SYSTEM

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_add_failed_dialog_title,
            R.string.widget_add_not_admin_dialog_message
        )
    }

    @Test
    fun testAddUserUseCaseReturnFailedDialogWhenUnknown() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.UNKNOWN

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_add_failed_dialog_title,
            R.string.widget_switch_failed_dialog_message
        )
    }

    @Test
    fun testAddUserUseCaseReturnNotAllowedDialogWhenOncallOrLocked() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.ONCALL_OR_LOCKED

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_add_failed_dialog_title,
            R.string.widget_add_not_allowed_dialog_message
        )
    }

    @Test
    fun testAddUserUseCaseReturnDisabledDialogWithSettingsActionWhenDisabledOnLockscreen() {
        // Arrange
        userRepository.addUserRestrictions = UserAddRestriction.DISABLED_ON_LOCKSCREEN

        // Act
        val result = addUserUseCase.invoke(testWidgetCategory)

        // Assert
        assertTrue("Result should be StartActivityUseCase", result is StartActivityUseCase)
        val intent = (result as StartActivityUseCase).intent
        assertRestrictedDialogIntentBase(intent)
        assertDialogContent(
            intent,
            R.string.widget_add_failed_dialog_title,
            R.string.widget_add_from_hub_dialog_message,
            expectSettingsAction = true
        )
    }
}
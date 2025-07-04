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

import android.content.Context;
import android.content.Intent;

import androidx.glance.appwidget.action.StartActivityIntentAction
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.model.UserSwitchRestrictions
import com.android.multiuser.widget.domain.DialogUseCase
import com.android.multiuser.widget.domain.NoAction
import com.android.multiuser.widget.domain.UserSwitchUseCase
import com.android.multiuser.widget.R

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class DialogUseCaseTest {

    @Test
    fun testSwitchSucceeded() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val userRepository = FakeUserRepository()
        userRepository.switchSucceeded = true
        val userSwitchUseCase = UserSwitchUseCase(11, userRepository, intentRepository)
        val action = userSwitchUseCase()
        assertTrue(action is NoAction)
    }

    @Test
    fun testSwitchFailed() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val userRepository = FakeUserRepository()
        userRepository.switchSucceeded = false
        val userSwitchUseCase = UserSwitchUseCase(11, userRepository, intentRepository)
        val action : StartActivityIntentAction = userSwitchUseCase() as StartActivityIntentAction
        assertNotNull(action.intent)
        val dialogUseCase = DialogUseCase(action.intent, context.resources)
        val model = dialogUseCase()
        val title: String = context.getString(R.string.widget_switch_failed_dialog_title)
        val message: String = context.getString(R.string.widget_switch_failed_dialog_message)
        assertEquals(model.title, title)
        assertEquals(model.message, message)
        assertNull(model.positiveButtonText)
        assertNull(model.actionIntent)
    }

    @Test
    fun testSwitchDisabledByUser() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val userRepository = FakeUserRepository()
        userRepository.userRestrictions = UserSwitchRestrictions.DISABLED
        val userSwitchUseCase = UserSwitchUseCase(11, userRepository, intentRepository)
        val action : StartActivityIntentAction = userSwitchUseCase() as StartActivityIntentAction
        assertNotNull(action.intent)
        val dialogUseCase = DialogUseCase(action.intent, context.resources)
        val model = dialogUseCase()
        val title: String = context.getString(R.string.widget_switch_disabled_dialog_title)
        val message: String = context.getString(R.string.widget_switch_disabled_dialog_message)
        val actionButton: String = context.getString(R.string.widget_switch_disabled_dialog_open_settings_button)
        assertEquals(model.title, title)
        assertEquals(model.message, message)
        assertEquals(model.positiveButtonText, actionButton)
        assertNotNull(model.actionIntent)
    }


    @Test
    fun testSwitchDisabledByWorkPolicy() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val userRepository = FakeUserRepository()
        userRepository.userRestrictions = UserSwitchRestrictions.WORK_POLICY
        val userSwitchUseCase = UserSwitchUseCase(11, userRepository, intentRepository)
        val action : StartActivityIntentAction = userSwitchUseCase() as StartActivityIntentAction
        assertNotNull(action.intent)
        val dialogUseCase = DialogUseCase(action.intent, context.resources)
        val model = dialogUseCase()
        val title: String = context.getString(R.string.widget_switch_work_restriction_dialog_title)
        val message: String = context.getString(R.string.widget_switch_work_restriction_dialog_message)
        assertEquals(model.title, title)
        assertEquals(model.message, message)
        assertNull(model.positiveButtonText)
        assertNull(model.actionIntent)
    }

    @Test
    fun testSwitchDisabledNotAllowedOnCall() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val userRepository = FakeUserRepository()
        userRepository.userRestrictions = UserSwitchRestrictions.ONCALL_OR_LOCKED
        val userSwitchUseCase = UserSwitchUseCase(11, userRepository, intentRepository)
        val action : StartActivityIntentAction = userSwitchUseCase() as StartActivityIntentAction
        assertNotNull(action.intent)
        val dialogUseCase = DialogUseCase(action.intent, context.resources)
        val model = dialogUseCase()
        val title: String = context.getString(R.string.widget_switch_not_allowed_dialog_title)
        val message: String = context.getString(R.string.widget_switch_not_allowed_dialog_message)
        assertEquals(model.title, title)
        assertEquals(model.message, message)
        assertNull(model.positiveButtonText)
        assertNull(model.actionIntent)
    }
}

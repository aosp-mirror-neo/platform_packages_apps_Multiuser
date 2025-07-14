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

import android.platform.test.annotations.RequiresFlagsEnabled;
import android.platform.test.annotations.RequiresFlagsDisabled;
import android.platform.test.flag.junit.CheckFlagsRule
import android.platform.test.flag.junit.DeviceFlagsValueProvider;

import androidx.glance.action.LambdaAction
import androidx.glance.appwidget.action.SendBroadcastIntentAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.domain.OpenUserSwitcherUseCase
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OpenUserSwitcherUseCaseTest {

    @get:Rule
    val checkFlagsRule = DeviceFlagsValueProvider.createCheckFlagsRule()

    @Test
    @RequiresFlagsEnabled(com.android.systemui.Flags.FLAG_MULTIUSER_OPEN_USER_SWITCHER_DIALOG)
    fun testOpenUserSwitcherUseCaseReturnBroadcastActionWhenFlagOn() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val openUserSwitcherUseCase = OpenUserSwitcherUseCase(intentRepository)
        assertTrue(openUserSwitcherUseCase() is SendBroadcastIntentAction)
    }

    @Test
    @RequiresFlagsDisabled(com.android.systemui.Flags.FLAG_MULTIUSER_OPEN_USER_SWITCHER_DIALOG)
    fun testOpenUserSwitcherUseCaseReturnLambdaActionWhenFlagOff() {
        val context = InstrumentationRegistry.getInstrumentation().getTargetContext()
        val intentRepository = IntentRepository(context.resources)
        val openUserSwitcherUseCase = OpenUserSwitcherUseCase(intentRepository)
        assertTrue(openUserSwitcherUseCase() is LambdaAction)
    }
}
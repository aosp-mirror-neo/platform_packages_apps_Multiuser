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

package com.android.multiuser.widget.ui.view

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.widget.ui.actions.ActionsStackView
import com.android.multiuser.widget.viewmodel.UserViewModel

@Composable
fun ContentStackView(item: UserViewModel, contentStack: Stack) {
    StackView(
        contentStack.list.mapIndexed { index, measurable ->
            val stack = measurable as Stack
            if (contentStack.list.size > 1 && index == 0) {
                { UserStackView(item, stack, GlanceModifier) }
            } else {
                { ActionsStackView(GlanceModifier, stack) }
            }
        },
        stack = contentStack,
        modifier = GlanceModifier
    )
}
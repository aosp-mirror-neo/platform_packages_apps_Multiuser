/*
 * Copyright (C) 2024 The Android Open Source Project
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
import androidx.glance.layout.Spacer
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.stacks.ui.height
import com.android.multiuser.stacks.ui.width
import com.android.multiuser.widget.viewmodel.measurables.SpacerMeasurable
import com.android.multiuser.widget.viewmodel.stacks.VerticalPaddingWrapperStack

@Composable
fun <T: Stack> VerticalPaddingWrapperStackView(
    stack: VerticalPaddingWrapperStack<T>,
    composeStack: @Composable () -> Unit,
    modifier: GlanceModifier
) {
    StackView(
        items = stack.list.map { measurable ->
            when (measurable) {
                is SpacerMeasurable -> {
                    // Return a lambda: @Composable () -> Unit
                    {
                        Spacer(
                            GlanceModifier.width(measurable.horizontally.min)
                                .height(measurable.vertically.min)
                        )
                    }
                } else -> {
                    composeStack // Return the composable stack
                }
            }
        },
        stack = stack,
        modifier = modifier
    )
}

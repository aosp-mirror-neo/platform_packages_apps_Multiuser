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
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.stacks.ui.cornerRadius
import com.android.multiuser.stacks.ui.height
import com.android.multiuser.stacks.ui.width
import com.android.multiuser.widget.viewmodel.measurables.IconMeasurable
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable
import com.android.multiuser.widget.viewmodel.stacks.ActionButtonStack
import kotlin.math.min

@Composable
fun StackButton(
        stack: ActionButtonStack,
        modifier: GlanceModifier = GlanceModifier.Companion,
        tint: ColorProvider = GlanceTheme.colors.onPrimary
    ) {
    StackView(
        // It's good practice to use mapNotNull here
        items = stack.list.mapNotNull { measurable ->
            when (measurable) {
                is IconMeasurable -> {
                    // Return a lambda: @Composable () -> Unit
                    {
                        Image(
                            provider = ImageProvider(measurable.resourceId),
                            contentDescription = if(stack.list.size == 1) { measurable.contentDescription } else { "" },
                            colorFilter = ColorFilter.tint(tint),
                            modifier = GlanceModifier
                                .width(measurable.horizontally.min)
                                .height(measurable.vertically.min)
                        )
                    }
                }

                is TextMeasurable -> {
                    // Return a lambda: @Composable () -> Unit
                    {
                        Text(
                            modifier = GlanceModifier
                                .width(measurable.horizontally.min)
                                .height(measurable.vertically.min),
                            text = measurable.text,
                            style = TextStyle(
                                color = tint,
                                fontSize = measurable.fontSize
                            ),
                            maxLines = 1
                        )
                    }
                }

                else -> null
            }
        },
        stack = stack,
        modifier = modifier
            .cornerRadius(
                (min(
                    stack.vertically.min,
                    stack.horizontally.min
                ) / 2)
            )
            .clickable(onClick = stack.action.action)
    )
}


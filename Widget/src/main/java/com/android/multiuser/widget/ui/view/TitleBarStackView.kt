package com.android.multiuser.widget.ui.view

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

import androidx.compose.runtime.Composable
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.background
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.android.multiuser.stacks.ui.height
import com.android.multiuser.stacks.ui.width
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.widget.viewmodel.measurables.IconMeasurable
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable
import com.android.multiuser.widget.viewmodel.stacks.ActionButtonStack
import com.android.multiuser.widget.viewmodel.stacks.TitleBarStack

@Composable
fun TitleBarStackView(stack: TitleBarStack) {
    StackView(stack.list.mapNotNull { measurable ->
        when (measurable) {
            is IconMeasurable -> {
                // Return a lambda: @Composable () -> Unit
                {
                    Image(
                        provider = ImageProvider(measurable.resourceId),
                        contentDescription = measurable.contentDescription,
                        colorFilter = ColorFilter.tint(GlanceTheme.colors.primary),
                        modifier = GlanceModifier
                            .width(measurable.horizontally.min)
                            .height(measurable.vertically.min)
                    )
                }
            }
            is TextMeasurable -> {
                // Return another lambda: @Composable () -> Unit
                {
                    Text(
                        text = measurable.text,
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = measurable.fontSize,
                            color = GlanceTheme.colors.onSurface
                        ),
                        modifier = GlanceModifier
                            .width(measurable.horizontally.min)
                            .height(measurable.vertically.min)
                    )
                }
            }
            is ActionButtonStack -> {
                {
                    StackButton(
                        measurable,
                        GlanceModifier.background(GlanceTheme.colors.secondaryContainer),
                        GlanceTheme.colors.onSecondaryContainer
                    )
                }
            }
            // mapNotNull will automatically filter out the nulls
            else -> null
        }
    }, stack, GlanceModifier.Companion)
}
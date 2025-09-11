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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.stacks.ui.cornerRadius
import com.android.multiuser.stacks.ui.height
import com.android.multiuser.stacks.ui.width
import com.android.multiuser.widget.ui.util.byState
import com.android.multiuser.widget.viewmodel.measurables.IconMeasurable
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable
import com.android.multiuser.widget.viewmodel.stacks.ActionButtonStack
import com.android.multiuser.widget.viewmodel.stacks.InternalButtonStack
import com.android.multiuser.widget.viewmodel.stacks.VerticalPaddingWrapperStack
import kotlin.math.min
import kotlinx.coroutines.launch

@Composable
fun StackButton(
        stack: ActionButtonStack,
        modifier: GlanceModifier = GlanceModifier,
        color: ColorProvider = GlanceTheme.colors.primary,
        tint: ColorProvider = GlanceTheme.colors.onPrimary
    ) {

    val cornerRadius = min(stack.vertically.min, stack.horizontally.min) / 2
    val state by stack.getStack().action.state.collectAsState()
    val background = color.byState(LocalContext.current, state.isEnabled())
    val foreground = tint.byState(LocalContext.current, state.isEnabled())
    val scope = rememberCoroutineScope()
    val prefs = currentState<Preferences>()

    scope.launch {
        stack.getStack().action.refresh(prefs)
    }

    VerticalPaddingWrapperStackView(
        stack as VerticalPaddingWrapperStack<InternalButtonStack>,

        // Return a lambda: @Composable () -> Unit
        {
            StackView(
                // It's good practice to use mapNotNull here
                items = stack.getStack().list.mapNotNull { measurable ->
                    when (measurable) {
                        is IconMeasurable -> {
                            // Return a lambda: @Composable () -> Unit
                            {
                                Image(
                                    provider = ImageProvider(measurable.resourceId),
                                    contentDescription = "",
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
                                        .height(measurable.vertically.min)
                                        .semantics { contentDescription = "" },
                                    text = measurable.text,
                                    style = TextStyle(
                                        color = foreground,
                                        fontSize = measurable.fontSize
                                    ),
                                    maxLines = 1
                                )
                            }
                        }

                        else -> null
                    }
                },
                stack = stack.getStack(),
                modifier = modifier.background(background).cornerRadius(cornerRadius)
            )
        },
        modifier = GlanceModifier
            .clickable( stack.getStack().action.action)
            .semantics {
                contentDescription =
                    stack.getStack().action.contentDescription ?: stack.getStack().action.title
            }
    )
}

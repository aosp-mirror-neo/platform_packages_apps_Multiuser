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

package com.android.multiuser.stacks.ui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import com.android.multiuser.stacks.Stack

/**
 * A Composable function that renders a [Stack] of items either horizontally or vertically.
 * It uses the properties of the provided [Stack] object (like orientation, dimensions,
 * padding, gutter, and alignment) to arrange the `items`.
 *
 * This view is designed for use with Glance AppWidgets.
 *
 * @param items A list of Composable functions, where each function defines an item in the stack.
 * @param stack The [Stack] model object containing layout information (dimensions, orientation,
 *              padding, gutter, alignment) for arranging the `items`.
 * @param modifier A [GlanceModifier] to be applied to the root layout of this StackView.
 *                 Defaults to an empty [GlanceModifier].
 */
@Composable
fun StackView (
    items: List<@Composable () -> Unit>,
    stack: Stack,
    modifier: GlanceModifier = GlanceModifier.Companion
) {
    var showSpacer = false

    if (stack is Stack.Horizontal) {
        // |    Composable     |      Spacer       |     Composable    |
        Row(
            modifier = modifier
                .width(stack.horizontally.min)
                .height(stack.vertically.min)
                .padding(stack.padding.horizontally.min, stack.padding.vertically.min),
            verticalAlignment = stack.alignment.vertical,
            horizontalAlignment = stack.alignment.horizontal
        ) {
            items.forEach { composable ->
                if (showSpacer) {
                    Spacer(GlanceModifier.size(stack.gutter))
                }
                showSpacer = true
                composable()
            }
        }
    } else {
        // |    Composable     |
        // |      Spacer       |
        // |     Composable    |
        Column(
            modifier = modifier
                .width(stack.horizontally.min)
                .height(stack.vertically.min)
                .padding(stack.padding.horizontally.min, stack.padding.vertically.min),
            verticalAlignment = stack.alignment.vertical,
            horizontalAlignment = stack.alignment.horizontal
        ) {
            items.forEach { composable ->
                if (showSpacer) {
                    Spacer(GlanceModifier.size(stack.gutter))
                }
                showSpacer = true
                composable()
            }
        }
    }
}
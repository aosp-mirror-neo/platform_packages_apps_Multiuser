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

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.stacks.ui.size
import com.android.multiuser.widget.R
import com.android.multiuser.widget.viewmodel.UiState
import com.android.multiuser.widget.viewmodel.stacks.UserStacks
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable
import kotlinx.coroutines.launch

/**
 * Displays user data in the widget.
 */
@Composable
fun UserStackView (
    viewModel: UserViewModel,
    stack: Stack,
    modifier: GlanceModifier,
) {
    val bitmap by viewModel.bitmap.collectAsState()
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    scope.launch {
        if (uiState != UiState.Loading) {
            viewModel.loadAvatar()
        }
    }
    StackView(
        items = stack.list.mapNotNull { measurable ->
            // Create a composable lambda for each item by wrapping in {}
            when (measurable) {
                is UserStacks.UserImageSquare -> {
                    // Return a lambda: @Composable () -> Unit
                    {
                        Image(
                            provider = bitmap?.let { ImageProvider(it) }
                                ?: ImageProvider(R.drawable.account_circle),
                            contentDescription = viewModel.contentDescription,
                            modifier = GlanceModifier.size(measurable.vertically.min)
                        )
                    }
                }
                is TextMeasurable -> {
                    // Return another lambda: @Composable () -> Unit
                    {
                        Text(
                            text = measurable.text,
                            maxLines = 1,
                            style = TextStyle(fontSize = measurable.fontSize),
                        )
                    }
                }
                // mapNotNull will automatically filter out the nulls
                else -> null
            }
        },
        stack = stack,
        modifier = modifier
    )
}
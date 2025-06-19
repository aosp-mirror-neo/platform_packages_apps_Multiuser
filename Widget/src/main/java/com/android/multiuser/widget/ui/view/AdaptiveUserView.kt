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
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.android.multiuser.widget.R
import com.android.multiuser.widget.ui.view.layout.AdaptivePane
import com.android.multiuser.widget.viewmodel.UiState
import com.android.multiuser.widget.viewmodel.UserStack
import com.android.multiuser.widget.viewmodel.UserViewModel
import kotlinx.coroutines.launch

/**
 * Displays user data in the widget.
 */
@Composable
fun AdaptiveUserView (
    viewModel: UserViewModel,
    userStack: UserStack,
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
    AdaptivePane(modifier = modifier,
        arrangement = userStack.arrangement,
        startPane = {
            Image(
            provider = bitmap?.let { ImageProvider(it) }
                ?: ImageProvider(R.drawable.account_circle),
            contentDescription = viewModel.contentDescription,
            modifier = GlanceModifier.size(userStack.imageSize)
        )},
        endPane = { if (userStack.textMetric.fontSize > 0.sp) {
            Text(
                text = viewModel.name,
                maxLines = 1,
                style = TextStyle(fontSize = userStack.textMetric.fontSize),
            )
        } else { null }
        })
}
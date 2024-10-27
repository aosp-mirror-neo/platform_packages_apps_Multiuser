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

package com.android.multiuser.widget.ui.view.layout

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.android.multiuser.widget.R
import com.android.multiuser.widget.ui.util.ResourceDp

@Composable
fun LoadingLayout() {
    Scaffold(
        titleBar = { WidgetTitleBar() },
        backgroundColor = GlanceTheme.colors.widgetBackground,
        horizontalPadding = ResourceDp(R.dimen.widget_grid_content_padding),
        modifier = GlanceModifier.padding(bottom = R.dimen.widget_grid_content_padding)
    ) {
        Box(
            modifier = GlanceModifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                GlanceModifier.size(
                    ResourceDp(R.dimen.loading_layout_circular_progress_indicator_size)
                ),
                GlanceTheme.colors.onSurface
            )
        }
    }
}

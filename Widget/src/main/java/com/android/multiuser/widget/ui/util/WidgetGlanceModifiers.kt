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

package com.android.multiuser.widget.ui.util

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentSize
import com.android.multiuser.R

object WidgetGlanceModifiers {
    @Composable
    fun TrailingBottomModifier() = GlanceModifier
        .width(ResourceDp(R.dimen.widget_grid_bottom_spacing))

    @Composable
    fun SelectedUserBoxModifier() = GlanceModifier
        .wrapContentSize()
        .padding(ResourceDp(R.dimen.widget_grid_content_padding))
        .background(
                ImageProvider(R.drawable.current_user_border),
                colorFilter = ColorFilter.tint(GlanceTheme.colors.primary))

    @Composable
    fun DefaultUserBoxModifier() = GlanceModifier
        .wrapContentSize()
        .padding(ResourceDp(R.dimen.widget_grid_content_padding))

    @Composable
    fun VerticalGridBoxModifier() = GlanceModifier
        .cornerRadius(ResourceDp(R.dimen.widget_item_corner_radius))
        .fillMaxSize()
}

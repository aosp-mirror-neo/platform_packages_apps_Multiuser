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

package com.android.multiuser.widget.ui.view.layout

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.layout.size
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.GlanceModifier
import com.android.multiuser.widget.viewmodel.Arrangement


/**
 * A layout focused on presenting a grid of images with titles. The list is
displayed in a [Scaffold] below a title bar.
 * @param model view model with users and actions data.
 */

@Composable
fun AdaptivePane(
    modifier: GlanceModifier = GlanceModifier,
    startPane: @Composable () -> Unit,
    endPane: @Composable () -> Unit,
    arrangement: Arrangement = Arrangement.Horizontal()
) {
    if (arrangement is Arrangement.Horizontal) {
        // |                                |
        // | Start Pane | Spacer | End Pane |
        // |                                |
        Row(modifier=modifier, verticalAlignment = Alignment.CenterVertically) {
            startPane()
            Spacer(GlanceModifier.size(arrangement.gutter))
            endPane()
        }
    } else {
        // |    Start Pane     |
        // |      Spacer       |
        // |     End Pane      |
        Column(modifier=modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            startPane()
            Spacer(GlanceModifier.size(arrangement.gutter))
            endPane()
        }
    }
}
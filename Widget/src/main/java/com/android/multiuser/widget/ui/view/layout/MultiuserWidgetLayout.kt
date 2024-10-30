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
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.GlanceTheme
import androidx.glance.layout.padding
import com.android.multiuser.widget.ui.grid.UsersView
import com.android.multiuser.widget.ui.util.ResourceDp
import com.android.multiuser.widget.R
import com.android.multiuser.widget.viewmodel.UsersViewModel

/**
 * A layout focused on presenting a grid of images. The list is displayed in a [Scaffold] below a
 * title bar.
 * @param items list of GridItemInfos to be displayed in the grid;a user avatar and name, or a guest
 * icon or a plus sign.
 * @param currentUserId current user id
 */
@Composable
fun MultiuserWidgetLayout(
    model: UsersViewModel,
) {
    Scaffold(
        titleBar = { WidgetTitleBar() },
        backgroundColor = GlanceTheme.colors.widgetBackground,
        horizontalPadding = ResourceDp(R.dimen.widget_grid_content_padding),
        modifier = GlanceModifier.padding(bottom = R.dimen.widget_grid_content_padding)
    ) {
        if (model.users.isEmpty()) {
            ErrorLayout()
        } else {
            UsersView(model)
        }
    }
}

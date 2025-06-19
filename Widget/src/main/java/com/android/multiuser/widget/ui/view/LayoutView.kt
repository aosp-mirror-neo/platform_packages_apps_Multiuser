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
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import com.android.multiuser.widget.ui.actions.ActionsStackView
import com.android.multiuser.widget.ui.view.layout.AdaptivePane
import com.android.multiuser.widget.ui.view.layout.WidgetTitleBar
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.Arrangement
import com.android.multiuser.widget.viewmodel.LayoutViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.UsersViewModel



@Composable
fun LayoutView(model: UsersViewModel) {
    val items by model.data.collectAsState()
    val actions = items.filterIsInstance<ActionViewModel>()
    items.filterIsInstance<UserViewModel>().firstOrNull()?.let { userViewModel ->
        val layout = LayoutViewModel(LocalSize.current, LocalContext.current.resources, userViewModel, actions)
        val titleBarVisible by layout.titleBarVisible.collectAsState()

        Scaffold(
            titleBar = if (titleBarVisible) {
                { WidgetTitleBar() }
            } else {
                null
            },
            backgroundColor = GlanceTheme.colors.widgetBackground
        ) {
            Row(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = if (layout.arrangement is Arrangement.Vertical) {
                    Alignment.CenterHorizontally
                    // |      🙃      |
                    // |     Name     |
                } else {
                    Alignment.Start
                    // | 🙃 Name      |
                }
            ) {
                AdaptivePane(
                    arrangement = layout.arrangement,
                    startPane = {
                        layout.userStack?.let { userStack ->
                            AdaptiveUserView(
                                viewModel = userViewModel,
                                userStack,
                                modifier = GlanceModifier
                            )
                        }
                    },
                    endPane = { ActionsStackView(GlanceModifier, layout.actionStack) })
            }
        }
    }
}

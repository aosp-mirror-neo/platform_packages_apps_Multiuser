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
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.StackView
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.LayoutViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.UsersViewModel
import com.android.multiuser.widget.viewmodel.stacks.TitleBarStack


@Composable
fun LayoutView(model: UsersViewModel) {
    val items by model.data.collectAsState()
    val actions = items.filterIsInstance<ActionViewModel>()
    items.filterIsInstance<UserViewModel>().firstOrNull()?.let { item ->
        val layout = LayoutViewModel(LocalSize.current, LocalContext.current.resources, item, actions)
        Box(modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.widgetBackground)) {

            val titleBar =
                layout.arrangement.list.firstOrNull { it is TitleBarStack } as? TitleBarStack
            if (titleBar != null) {
                val contentStack = layout.arrangement.list.last() as Stack
                StackView(
                    listOf(
                        { TitleBarStackView(titleBar) },
                        { ContentStackView(item, contentStack) }),
                    layout.arrangement,
                    GlanceModifier
                )
            } else {
                ContentStackView(item, layout.arrangement)
            }
        }
    }
}

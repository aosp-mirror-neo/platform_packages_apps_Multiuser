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

package com.android.multiuser.widget.viewmodel

import android.content.res.Resources
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.android.multiuser.widget.R
import com.android.multiuser.widget.util.dp

class LayoutViewModel(
    var size: DpSize, val res: Resources, val userViewModel: UserViewModel
) {
    val titleBarVisible: StateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val arrangement = if (size.width <= size.height) {
        Arrangement.Vertical()
    } else {
        Arrangement.Horizontal()
    }
    var userStack: UserStack? = null
    val actionStackHeight = 56.dp //TODO: Implement b/409212274
    val actionStackWidth = 56.dp //TODO: Implement b/409212274
    val widgetPadding = res.dp(R.dimen.content_padding) * 2// top+bottom or left+right

    // with title bar visible we do not set top padding
    val titleBarHeight = res.dp(R.dimen.title_bar_height) - res.dp(R.dimen.content_padding)
    val userStacks = UserStacks(res, userViewModel.name)

    init {
        if (arrangement is Arrangement.Vertical) {
            // | Users ⚙️|
            // |=========|
            // |   🙃    |
            // |  Name   |
            // |  🔄 ➕  |
            var verticalHeight = actionStackHeight + widgetPadding
            userStack =
                listOf(
                    userStacks.SMALL_VERTICAL,
                    userStacks.LARGE_VERTICAL
                ).filter { it.height + verticalHeight + titleBarHeight <= (size.height) }
                    .maxByOrNull { it.height }
            if (userStack == null) { // title bar does not fit, check without title bar
                (titleBarVisible as MutableStateFlow<Boolean>).value = false
                // |   🙃    |
                // |  Name   |       |   🙃    |
                // |  🔄 ➕  |       |  🔄 ➕  |
                userStack = listOf(
                    userStacks.SMALL_IMAGE_ONLY,
                    userStacks.SMALL_VERTICAL,
                    userStacks.LARGE_VERTICAL
                ).filter { it.height + verticalHeight <= (size.height) }
                    .maxByOrNull { it.height }
                if (userStack == null) { // user name does not fit, use small image only
                    // |  🔄 ➕  |
                }
            } else { // show title bar
                (titleBarVisible as MutableStateFlow<Boolean>).value = true
            }
        } else { // Large vertical user stack has priority in horizontal layout
            // | Users    ⚙️ |
            // |=============|
            // |   🙃   | 🔄 |
            // |  Name  | ➕ |
            var horizontalWidth = actionStackWidth + widgetPadding
            if (userStacks.LARGE_VERTICAL.height + titleBarHeight + widgetPadding <= (size.height)
                && (userStacks.LARGE_VERTICAL.width + horizontalWidth <= size.width)) {
                (titleBarVisible as MutableStateFlow<Boolean>).value = true
                userStack = userStacks.LARGE_VERTICAL
            } else { // Vertical layout does not fit, try all others
                // | Users         ⚙️ |    | Users    ⚙️ |    | Users    ⚙️ |    | Users  ⚙️ |
                // |==================|    |=============|    |   🙃   | 🔄 |    |===========|
                // | 🙃 Name | 🔄  ➕ |    | 🙃 | 🔄  ➕ |    |  Name  | ➕ |    |  🙃  | ➕ |
                userStack = listOf(
                    userStacks.SMALL_HORIZONTAL,
                    userStacks.LARGE_HORIZONTAL,
                    userStacks.SMALL_VERTICAL,
                    userStacks.SMALL_IMAGE_ONLY
                ).filter {
                        (it.height + titleBarHeight + widgetPadding <= (size.height))
                                && (it.width + horizontalWidth <= size.width)
                    }.maxByOrNull { it.width }
                (titleBarVisible as MutableStateFlow<Boolean>).value = true
                if (userStack == null) {// Title bar does not fit, try without
                    // | 🙃 Name | 🔄  ➕ |        | 🙃 | 🔄  ➕ |
                    userStack = listOf(
                        userStacks.SMALL_HORIZONTAL,
                        userStacks.LARGE_HORIZONTAL,
                        userStacks.SMALL_IMAGE_ONLY
                    ).filter {
                            (it.height + widgetPadding <= (size.height))
                                    && (it.width + horizontalWidth <= size.width)
                        }.maxByOrNull { it.width }
                    (titleBarVisible as MutableStateFlow<Boolean>).value = false
                }
                if (userStack == null) {
                    // user name does not fit, show buttons only
                    // |  🔄 ➕  |
                }
            }
        }
    }
}
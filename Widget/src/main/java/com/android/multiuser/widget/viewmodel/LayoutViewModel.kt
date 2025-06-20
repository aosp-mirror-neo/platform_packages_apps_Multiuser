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
import androidx.compose.ui.unit.DpSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.android.multiuser.widget.R
import com.android.multiuser.widget.viewmodel.stacks.ActionStacks
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.dp
import com.android.multiuser.stacks.ui.inDp
import com.android.multiuser.stacks.ui.toPx
import com.android.multiuser.widget.viewmodel.stacks.TitleBarStack
import com.android.multiuser.widget.viewmodel.stacks.UserStacks

class LayoutViewModel(
    var size: DpSize, val res: Resources,  userViewModel: UserViewModel,
     actionViewModelList: List<ActionViewModel>
) {
    val titleBarVisible: StateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    val arrangement = if (size.width <= size.height) {
        Arrangement.Vertical()
    } else {
        Arrangement.Horizontal()
    }
    var userStack: Stack? = null
    val widgetPadding = res.dp(R.dimen.content_padding) * 2// top+bottom or left+right

    // with title bar visible we do not set top padding
    val titleBarHeight = res.dp(R.dimen.title_bar_height) - res.dp(R.dimen.content_padding)
    val userStacks = UserStacks(res, userViewModel)
    val titleBarStack = TitleBarStack.Large(res)
        .takeIf { it.horizontally.min <= size.width.toPx(res.displayMetrics)}
        ?: TitleBarStack.Small(res)

    // TODO: replace with more stacks when user stacks are measurable
    val actionStack =  ActionStacks(res, actionViewModelList).TALL_HORIZONTAL
    val actionStackHeight = actionStack.vertically.min.inDp(res.displayMetrics)
    val actionStackWidth = actionStack.horizontally.min.inDp(res.displayMetrics)

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
                ).filter { it.vertically.min.inDp(res.displayMetrics) + verticalHeight + titleBarHeight <= (size.height) }
                    .maxByOrNull { it.vertically.min.inDp(res.displayMetrics) }
            if (userStack == null) { // title bar does not fit, check without title bar
                (titleBarVisible as MutableStateFlow<Boolean>).value = false
                // |   🙃    |
                // |  Name   |       |   🙃    |
                // |  🔄 ➕  |       |  🔄 ➕  |
                userStack = listOf(
                    userStacks.SMALL_IMAGE_ONLY,
                    userStacks.SMALL_VERTICAL,
                    userStacks.LARGE_VERTICAL
                ).filter { it.vertically.min.inDp(res.displayMetrics) + verticalHeight <= (size.height) }
                    .maxByOrNull { it.vertically.min.inDp(res.displayMetrics) }
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
            if (userStacks.LARGE_VERTICAL.vertically.min.inDp(res.displayMetrics) + titleBarHeight + widgetPadding <= (size.height)
                && (userStacks.LARGE_VERTICAL.horizontally.min.inDp(res.displayMetrics) + horizontalWidth <= size.width)) {
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
                        (it.vertically.min.inDp(res.displayMetrics) + titleBarHeight + widgetPadding <= (size.height))
                                && (it.horizontally.min.inDp(res.displayMetrics) + horizontalWidth <= size.width)
                    }.maxByOrNull { it.horizontally.min.inDp(res.displayMetrics) }
                (titleBarVisible as MutableStateFlow<Boolean>).value = true
                if (userStack == null) {// Title bar does not fit, try without
                    // | 🙃 Name | 🔄  ➕ |        | 🙃 | 🔄  ➕ |
                    userStack = listOf(
                        userStacks.SMALL_HORIZONTAL,
                        userStacks.LARGE_HORIZONTAL,
                        userStacks.SMALL_IMAGE_ONLY
                    ).filter {
                            (it.vertically.min.inDp(res.displayMetrics) + widgetPadding <= (size.height))
                                    && (it.horizontally.min.inDp(res.displayMetrics) + horizontalWidth <= size.width)
                        }.maxByOrNull { it.horizontally.min.inDp(res.displayMetrics) }
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
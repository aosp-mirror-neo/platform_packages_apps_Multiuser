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
import com.android.multiuser.stacks.FixedRect
import kotlinx.coroutines.flow.MutableStateFlow
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.toPx
import com.android.multiuser.widget.viewmodel.stacks.LayoutStacks

class LayoutViewModel(
    var size: DpSize,
    val res: Resources,
    userViewModel: UserViewModel,
    actionViewModelList: List<ActionViewModel>
) {
    val layoutSize = FixedRect(size.width.toPx(res.displayMetrics), size.height.toPx(res.displayMetrics))
    val titleBarVisible = MutableStateFlow(false)
    val isVertical = (layoutSize.horizontally.min <= layoutSize.vertically.min)
    var arrangement: Stack

    init {
        val layouts = LayoutStacks(res, userViewModel, actionViewModelList)
        val availableHeight = layoutSize.vertically.min
        val availableWidth = layoutSize.horizontally.min
        arrangement = layouts.fallbackShort()
        titleBarVisible.value = false
        if (isVertical) {
            listOf(layouts.tall(), layouts.tallSlim(), layouts.slim())
                .filter { it.vertically.min<= availableHeight
                        && it.horizontally.min <= availableWidth }
                .maxByOrNull { it.horizontally.min * it.vertically.min }
                ?.let {
                    arrangement = it
                    arrangement.vertically.max = availableHeight
                }
        } else {
            listOf(layouts.tallWide(), layouts.wide(), layouts.default(), layouts.shortWide(),
                layouts.short(), layouts.shortSlim())
                .filter { it.vertically.min <= availableHeight
                        && it.horizontally.min <= availableWidth }
                .maxByOrNull { it.horizontally.min * it.vertically.min }
                ?.let {
                    arrangement = it
                    arrangement.vertically.max = availableHeight
                }
        }
        arrangement.autoLayout(layoutSize.horizontally.max, layoutSize.vertically.max)
    }
}

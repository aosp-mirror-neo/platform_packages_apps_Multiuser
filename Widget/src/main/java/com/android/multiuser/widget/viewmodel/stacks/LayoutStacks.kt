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

package com.android.multiuser.widget.viewmodel.stacks

import android.content.res.Resources
import androidx.glance.layout.Alignment
import com.android.multiuser.stacks.FixedRect
import com.android.multiuser.widget.R
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.px
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel

class LayoutStacks(val res: Resources, item: UserViewModel, actions: List<ActionViewModel>) {
    val userStacks = UserStacks(res, item)
    val actionStacks = ActionStacks(res, actions)

    open class VerticalLayoutStack(val res: Resources, list: List<Stack>) :
        Stack.Vertical(
            res.px(R.dimen.content_vertical_gutter),
            res.layoutPadding(),
            list
        )

    class VerticalLayoutStackWithTitleBar(res: Resources, list: List<Stack>) : Stack.Vertical(
        0f, FixedRect(0f, 0f),
        listOf(
            TitleBarStack.Small(res),
            VerticalLayoutStack(res, list)
        )
    ) {
        init {
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
        }
    }

    class VerticalLayoutStackWithLargeTitleBar(res: Resources, list: List<Stack>) : Stack.Vertical(
        0f, FixedRect(0f, 0f),
        listOf(
            TitleBarStack.Large(res),
            VerticalLayoutStack(res, list)
        )
    ) {
        init {
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
        }
    }


    class HorizontalLayoutStackWithLargeTitleBar(
        res: Resources,
        list: List<Stack>
    ) : Stack.Vertical(
        0f, FixedRect(0f, 0f),
        listOf(TitleBarStack.Large(res),
            HorizontalLayoutStack(res, list)
                .apply {
                    alignment = Alignment.Center
                    vertically.stretchToFill = true
                })
    ) {
        init {
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
        }
    }

    class HorizontalLayoutStack(
        val res: Resources,
        list: List<Stack>
    ) : Stack.Horizontal(
        res.px(R.dimen.content_horizontal_gutter),
        res.layoutPadding(),
        list
    ) {
        init {
            list.first().vertically.max.let { listMax ->
                list.forEach {
                    if (listMax > it.vertically.min) {
                        it.vertically.max = listMax
                    }
                }
            }
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
        }
    }

    fun tallWide() = HorizontalLayoutStackWithLargeTitleBar(
        res,
        listOf(userStacks.LARGE_VERTICAL, actionStacks.LARGE_VERTICAL)
    ).apply {
        TAG = "Tall Wide"
        horizontally.stretchToFill = true
        vertically.stretchToFill = true
    }
    // To be used with and without title bar

    fun wide() = HorizontalLayoutStackWithLargeTitleBar(
        res,
        listOf(userStacks.LARGE_HORIZONTAL, actionStacks.TALL_HORIZONTAL)
    ).apply {
        TAG = "Wide"
        horizontally.stretchToFill = true
        vertically.stretchToFill = true
    }

    fun shortWide() = HorizontalLayoutStack(
        res,
        listOf(userStacks.LARGE_HORIZONTAL, actionStacks.TALL_HORIZONTAL)
    ).apply { TAG = "Short Wide" }

    fun default() = HorizontalLayoutStackWithLargeTitleBar(
        res,
        listOf(userStacks.SMALL_VERTICAL, actionStacks.SMALL_VERTICAL)
    ).apply { TAG = "Default" }

    fun short() = HorizontalLayoutStack(
        res,
        listOf(userStacks.SMALL_IMAGE_ONLY, actionStacks.TALL_HORIZONTAL)
    ).apply { TAG = "Short" }

    fun shortSlim() = HorizontalLayoutStack(
        res,
        listOf(userStacks.SMALL_IMAGE_ONLY, actionStacks.SINGLE_HORIZONTAL)
    ).apply { TAG = "Short Slim" }

    fun fallback() = HorizontalLayoutStack(
        res,
        listOf(actionStacks.TALL_HORIZONTAL)
    ).apply { TAG = "Fallback" }

    fun fallbackShort() = HorizontalLayoutStack(
        res, listOf(actionStacks.SINGLE_HORIZONTAL)
    ).apply { TAG = "Fallback Short" }

    fun tall() = VerticalLayoutStackWithLargeTitleBar(
        res, listOf(userStacks.SMALL_VERTICAL, actionStacks.HORIZONTAL)
    ).apply { TAG = "Tall" }

    fun tallSlim() = VerticalLayoutStackWithTitleBar(
        res, listOf(userStacks.SMALL_VERTICAL, actionStacks.HORIZONTAL)
    ).apply { TAG = "Tall Slim" }

    fun slim() = VerticalLayoutStack(
        res,
        listOf(userStacks.SMALL_IMAGE_ONLY, actionStacks.HORIZONTAL)
    ).apply { TAG = "Slim" }
}
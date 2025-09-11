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
        ) {
        init {
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
        }
    }

    class VerticalLayoutStackWithTitleBar(res: Resources, list: List<Stack>) : Stack.Vertical(
        0f, FixedRect(0f, 0f),
        listOf(
            TitleBarStack.Small(res),
            VerticalLayoutStack(res, list)
        )
    ) {
        init {
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
        listOf(userStacks.largeVertical(), actionStacks.largeVertical())
    ).apply {
        TAG = "Tall Wide"
        horizontally.stretchToFill = true
        vertically.stretchToFill = true
    }
    // To be used with and without title bar

    fun wide() = HorizontalLayoutStackWithLargeTitleBar(
        res,
        listOf(userStacks.horizontal(), actionStacks.tallHorizontal())
    ).apply {
        TAG = "Wide"
        horizontally.stretchToFill = false
        vertically.stretchToFill = true
    }

    fun shortWide() = HorizontalLayoutStack(
        res,
        listOf(userStacks.horizontal(), actionStacks.tallHorizontal())
    ).apply { TAG = "Short Wide" }

    fun default() = HorizontalLayoutStackWithLargeTitleBar(
        res,
        listOf(userStacks.vertical(), actionStacks.smallVertical())
    ).apply { TAG = "Default" }

    fun short() = HorizontalLayoutStack(
        res,
        listOf(userStacks.smallImageOnly(), actionStacks.tallHorizontal())
    ).apply { TAG = "Short"
        horizontally.stretchToFill = true
        vertically.stretchToFill = true }

    fun shortSlim() = HorizontalLayoutStack(
        res,
        listOf(userStacks.smallImageOnly(), actionStacks.singleHorizontal())
    ).apply {
        TAG = "Short Slim"
        horizontally.stretchToFill = true
        vertically.stretchToFill = true
    }

    fun fallback() = HorizontalLayoutStack(
        res,
        listOf(actionStacks.tallHorizontal())
    ).apply { TAG = "Fallback" }

    fun fallbackShort() = HorizontalLayoutStack(
        res, listOf(actionStacks.singleHorizontal())
    ).apply { TAG = "Fallback Short" }

    fun tallLarge() = VerticalLayoutStackWithLargeTitleBar(
        res, listOf(userStacks.largeVertical(), actionStacks.largeVertical())
    ).apply { TAG = "Tall Large" }

    fun tall() = VerticalLayoutStackWithLargeTitleBar(
        res, listOf(userStacks.smallVertical(), actionStacks.horizontal())
    ).apply { TAG = "Tall" }

    fun tallSlim() = VerticalLayoutStackWithTitleBar(
        res, listOf(userStacks.smallVertical(), actionStacks.horizontal())
    ).apply { TAG = "Tall Slim" }


    fun taller() = VerticalLayoutStackWithLargeTitleBar(
        res, listOf(userStacks.largeVertical(), actionStacks.horizontal())
    ).apply { TAG = "Taller" }

    fun tallerSlim() = VerticalLayoutStackWithTitleBar(
        res, listOf(userStacks.largeVertical(), actionStacks.horizontal())
    ).apply {
        TAG = "Taller Slim"
    }

    fun slim() = VerticalLayoutStack(
        res,
        listOf(userStacks.smallImageOnly(), actionStacks.horizontal())
    ).apply {
        TAG = "Slim"
    }
}
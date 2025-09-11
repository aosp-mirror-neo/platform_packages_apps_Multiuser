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
import com.android.multiuser.stacks.FixedSquare
import com.android.multiuser.stacks.Stack
import com.android.multiuser.stacks.ui.px
import com.android.multiuser.widget.R
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable

class UserStacks(val res: Resources, val viewModel: UserViewModel) {
    private fun mediumText() = TextMeasurable(res, R.dimen.user_text_min_size, viewModel.name)
    private fun largeText() = TextMeasurable(res, R.dimen.user_text_max_size, viewModel.name)

    val mediumImageSize: Float = res.px(R.dimen.user_image_min_size)
    val largeImageSize = res.px(R.dimen.user_image_max_size)
    val verticalGutter = res.px(R.dimen.user_vertical_gutter)
    val horizontalGutter = res.px(R.dimen.user_horizontal_gutter)

    class UserImageSquare(val viewModel: UserViewModel, imageSize: Float) :FixedSquare (imageSize)

    fun mediumImageMeasurable() = UserImageSquare(viewModel, mediumImageSize)
    fun largeImageMeasurable() = UserImageSquare(viewModel, largeImageSize)

    fun smallImageOnly() = Stack.Vertical(0f, list = listOf(mediumImageMeasurable()))
        .apply {
            TAG = "USER Small Image Only"
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
            alignment = Alignment.Center
        }
    fun smallVertical() = Stack.Vertical(verticalGutter, list = listOf(mediumImageMeasurable(), mediumText()))
        .apply {
            horizontally.stretchToFill = false
            vertically.stretchToFill = true
            alignment = Alignment.Center
            TAG = "USER Small Vertical"
        }

    fun vertical() = Stack.Vertical(verticalGutter, list = listOf(mediumImageMeasurable(), mediumText()))
        .apply {
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
            alignment = Alignment.Center
            TAG = "USER Small Vertical"
        }
    fun largeVertical() = Stack.Vertical(verticalGutter, list = listOf(largeImageMeasurable(), largeText()))
        .apply {
            TAG = "USER Large Horizontal"
            horizontally.stretchToFill = true
            vertically.stretchToFill = true
            alignment = Alignment.Center
        }

    fun horizontal() = Stack.Horizontal(horizontalGutter, list = listOf(mediumImageMeasurable(), largeText()))
        .apply {
            TAG = "USER Horizontal"
            horizontally.stretchToFill = true
        }
}


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
import com.android.multiuser.stacks.Measurable
import com.android.multiuser.stacks.Resizable
import com.android.multiuser.stacks.ui.px
import com.android.multiuser.widget.R


fun Resources.actionButtonGutter() = px(R.dimen.action_button_gutter)
fun Resources.actionHorizontalGutter() = px(R.dimen.action_stack_horizontal_gutter)
fun Resources.actionVerticalGutter() = px(R.dimen.action_stack_vertical_gutter)
fun Resources.actionLargePadding() = px(R.dimen.action_stack_large_vertical_padding)

fun Resources.actionButtonPadding() : Measurable {
    val actionVerticalPadding = Resizable( px(R.dimen.action_vertical_padding_min),
        px(R.dimen.action_vertical_padding_max))
    val actionHorizontalPadding = Resizable( px(R.dimen.action_horizontal_padding_min),
        px(R.dimen.action_horizontal_padding_max))
    return Measurable(actionHorizontalPadding, actionVerticalPadding)
}

fun Resources.tallActionButtonPadding() : Measurable {
    val actionVerticalPadding = Resizable( px(R.dimen.action_vertical_padding_min),
        px(R.dimen.action_vertical_padding_max))
    val actionHorizontalPadding = Resizable( px(R.dimen.action_horizontal_padding_min),
        px(R.dimen.action_horizontal_padding_min))
    return Measurable(actionHorizontalPadding, actionVerticalPadding)
}

fun Resources.wideActionButtonPadding() : Measurable {
    val actionVerticalPadding = Resizable( px(R.dimen.action_vertical_padding_min),
        px(R.dimen.action_vertical_padding_min))
    val actionHorizontalPadding = Resizable( px(R.dimen.action_horizontal_padding_min),
        px(R.dimen.action_horizontal_padding_max))
    return Measurable(actionHorizontalPadding, actionVerticalPadding)
}

fun Resources.largeActionButtonPadding() : Measurable {
    val actionVerticalPadding = Resizable( px(R.dimen.action_vertical_padding_min),
        px(R.dimen.action_vertical_padding_min))
    val actionHorizontalPadding = Resizable( px(R.dimen.action_horizontal_padding_min),
        px(R.dimen.large_action_button_side_padding))
    return Measurable(actionHorizontalPadding, actionVerticalPadding)
}
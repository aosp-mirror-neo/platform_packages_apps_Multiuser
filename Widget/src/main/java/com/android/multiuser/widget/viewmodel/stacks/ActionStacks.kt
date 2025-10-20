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
import com.android.multiuser.widget.viewmodel.ActionViewModel

class ActionStacks (val res: Resources, val actions:List<ActionViewModel>) {

    fun wideButtons() = actions.mapIndexed { index, action ->
        ActionButtonStack(res, action, res.largeActionButtonPadding(), false, index == 0)
    }.also { buttons ->
        val max = buttons.minBy { it.horizontally.max }.horizontally.max
        buttons.forEach { button ->
            button.horizontally.max = max
        }
        buttons.first().apply{
           getStack().isPrimary = true
        }
    }

    fun tallButtonsNoText() = actions.mapIndexed { index, action ->
        ActionButtonStack(res, action, res.tallActionButtonPadding(), isPrimary = index == 0)
    }

    fun wideButtonsNoText() = actions.mapIndexed {index, action ->
        ActionButtonStack(res, action, res.wideActionButtonPadding(), isPrimary = index == 0)
    }

    fun buttonsNoText() = actions.mapIndexed { index, action ->
        ActionButtonStack(res, action, res.actionButtonPadding(), isPrimary = index == 0)
    }

    fun largeVertical() = VerticalActionStack(res, wideButtons())
        .apply { TAG = "ACTION Large Vertical" }

    fun largeHorizontal() = HorizontalActionStack(res, wideButtons())
        .apply { TAG = "ACTION Large Horizontal" }

    fun singleHorizontal() = HorizontalActionStack(0f, 0f, tallButtonsNoText().subList(0,1))
        .apply { TAG = "ACTION Single Horizontal"  }

    fun tallHorizontal() = HorizontalActionStack(res, tallButtonsNoText())
        .apply { TAG = "ACTION Tall Horizontal" }

    fun wideHorizontal() = HorizontalActionStack(res, wideButtonsNoText())
        .apply { TAG = "ACTION Wide Horizontal" }

    fun smallVertical() = VerticalActionStack(res, wideButtonsNoText())
        .apply { TAG = "ACTION Small Vertical" }

    fun horizontal() = HorizontalActionStack(res, buttonsNoText())
        .apply { TAG = "ACTION Horizontal" }

}
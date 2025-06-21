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

    val WIDE_BUTTONS = actions.mapIndexed { index, action ->
        ActionButtonStack(res, action, res.largeActionButtonPadding(), false, index == 0)
    }.also { buttons ->
        val max = buttons.minBy { it.horizontally.max }.horizontally.max
        buttons.forEach { button ->
            button.horizontally.max = max
        }
        buttons.first().apply{
            isPrimary = true
        }
    }

    val TALL_BUTTON_NO_TEXTS = actions.mapIndexed { index, action ->
        ActionButtonStack(res, action, res.tallActionButtonPadding(), isPrimary = index == 0)
    }

    val WIDE_BUTTONS_NO_TEXT = actions.mapIndexed {index, action ->
        ActionButtonStack(res, action, res.wideActionButtonPadding(), isPrimary = index == 0)
    }

    val BUTTONS_NO_TEXT = actions.mapIndexed { index, action ->
        ActionButtonStack(res, action, res.actionButtonPadding(), isPrimary = index == 0)
    }

    val LARGE_VERTICAL = VerticalActionStack(res.actionVerticalGutter(), res.actionLargePadding(),
        WIDE_BUTTONS).apply { TAG = "ACTION Large Vertical" }
    val SINGLE_HORIZONTAL = HorizontalActionStack(0f, 0f,
        TALL_BUTTON_NO_TEXTS.subList(0,1)).apply { TAG = "ACTION Single Horizontal"  }
    val TALL_HORIZONTAL = HorizontalActionStack(res, TALL_BUTTON_NO_TEXTS)
        .apply { TAG = "ACTION Tall Horizontal" }
    val WIDE_HORIZONTAL = HorizontalActionStack(res, WIDE_BUTTONS_NO_TEXT)
        .apply { TAG = "ACTION Wide Horizontal" }
    val SMALL_VERTICAL = VerticalActionStack(res, WIDE_BUTTONS_NO_TEXT)
        .apply { TAG = "ACTION Small Vertical" }
    val HORIZONTAL = HorizontalActionStack(res, BUTTONS_NO_TEXT)
        .apply { TAG = "ACTION Horizontal" }
}
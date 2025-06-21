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
import com.android.multiuser.widget.R
import com.android.multiuser.stacks.Measurable
import com.android.multiuser.stacks.Stack
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable
import com.android.multiuser.widget.viewmodel.measurables.toIconMeasurable
import kotlin.math.min

class ActionButtonStack(val res: Resources,
    val action: ActionViewModel,
    padding: Measurable,
    iconOnly: Boolean = true,
    var isPrimary: Boolean = false ) : Stack.Horizontal(res.actionButtonGutter(),
        padding, if (iconOnly) {
            listOf (action.toIconMeasurable(res))
        } else {
            listOf (action.toIconMeasurable(res), TextMeasurable(res, R.dimen.user_text_min_size, action.title))
        }) {
    init {
        alignment = Alignment.Center
    }

    override var TAG = "ActionStack ${action.title}"
    override fun increaseVerticalMin(totalAvailable: Float) : Float {
        this.vertically.min = min(this.vertically.max, totalAvailable)
        return this.vertically.min
    }

    override fun increaseHorizontalMin(totalAvailable: Float) : Float {
        this.horizontally.min = min(this.horizontally.max, totalAvailable)
        return this.vertically.min
    }
}
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

package com.android.multiuser.widget.viewmodel.measurables

import android.content.res.Resources
import com.android.multiuser.widget.R
import com.android.multiuser.stacks.FixedSquare
import com.android.multiuser.stacks.ui.px
import com.android.multiuser.widget.viewmodel.ActionViewModel

class IconMeasurable (val resourceId: Int, val contentDescription: String, iconSize: Float)
    : FixedSquare(iconSize) {

    override fun toString(): String {
        return "IconMetric $contentDescription ${super.toString()}"
    }
}

fun ActionViewModel.toIconMeasurable(res: Resources) = IconMeasurable(resourceId,
    contentDescription ?: title, res.px(R.dimen.action_icon_size))
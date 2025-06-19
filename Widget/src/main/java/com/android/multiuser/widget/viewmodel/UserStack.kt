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

import androidx.compose.ui.unit.Dp
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable

/**
 * Single User Stack = person image + person name
 */
class UserStack(val arrangement: Arrangement, val imageSize: Dp, val textMetric: TextMeasurable) {
    val width = if(arrangement is Arrangement.Horizontal) {
        imageSize + textMetric.width + arrangement.gutter + arrangement.padding
    } else {
        imageSize + arrangement.padding
    }

    val height = if(arrangement is Arrangement.Vertical) {
        imageSize + textMetric.height + arrangement.gutter + arrangement.padding
    } else {
        imageSize + arrangement.padding
    }

    override fun toString(): String {
        return if(arrangement is Arrangement.Vertical) {"V"} else {"H"} + " ${width} x ${height} imageSize: $imageSize gutter: ${arrangement.gutter}\ntext: ${textMetric.width}x${textMetric.height}"
    }
}
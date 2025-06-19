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
import android.text.TextPaint
import androidx.annotation.DimenRes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import com.android.multiuser.stacks.Fixed
import com.android.multiuser.stacks.Measurable
import com.android.multiuser.stacks.Resizable
import com.android.multiuser.stacks.ui.sp
import com.android.multiuser.stacks.ui.toPx
import com.android.multiuser.stacks.ui.inDp

open class TextMeasurable(val res: Resources, @DimenRes val textSizeId: Int? = null, val text: String = "")
    : Measurable(Resizable(0f,0f), Fixed(0f)) {
    val fontSize: TextUnit = textSizeId?.let { res.sp(it) } ?: 0.sp

    init {
        textSizeId?.let { resId ->
            val textPaint = TextPaint(TextPaint.FAKE_BOLD_TEXT_FLAG).apply {
                textSize = fontSize.toPx(res.displayMetrics)
            }
            // Add 10% to start and end to avoid ellipsis
            val safetyBuffer = (fontSize.toPx(res.displayMetrics) / 5)
            val minWidth = textPaint.measureText(text) + safetyBuffer
            horizontally.min = minWidth
            horizontally.max = minWidth
            val minHeight = textPaint.fontMetrics.bottom - textPaint.fontMetrics.top
            vertically.min = minHeight
            vertically.max = minHeight
        }
    }

    val width: Dp
        get() = horizontally.min.inDp(res.displayMetrics)

    val height: Dp
        get() = vertically.min.inDp(res.displayMetrics)

}
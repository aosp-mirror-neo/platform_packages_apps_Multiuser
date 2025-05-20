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

import android.content.res.Resources
import androidx.annotation.DimenRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.multiuser.widget.util.sp
import com.android.multiuser.widget.util.spToDp

class TextMetric(res: Resources, @DimenRes textSizeId: Int? = null, text: String = "") {
    val size: TextUnit = textSizeId?.let { res.sp(it) } ?: 0.sp
    val height: Dp = textSizeId?.let { res.spToDp(it) } ?: 0.dp
    // TODO: Verify how text min width should be calculated
    val width = 100.dp

    override fun toString() = "TextMetric size: $size, height: $height, width: $width"
}
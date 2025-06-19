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
import androidx.compose.ui.unit.Dp
import com.android.multiuser.widget.R
import com.android.multiuser.stacks.ui.dp
import com.android.multiuser.widget.viewmodel.measurables.TextMeasurable

class UserStacks(val res: Resources,val text:String) {
    private val NONE_TEXT = TextMeasurable(res)
    private val MEDIUM_TEXT = TextMeasurable(res, R.dimen.user_text_min_size)
    private val LARGE_TEXT = TextMeasurable(res, R.dimen.user_text_max_size)

    val mediumImageSize: Dp = res.dp(R.dimen.user_image_min_size)
    val largeImageSize = res.dp(R.dimen.user_image_max_size)
    val verticalGutter = res.dp(R.dimen.user_vertical_gutter)
    val verticalArrangement = Arrangement.Vertical(verticalGutter)
    val horizontalGutter = res.dp(R.dimen.user_horizontal_gutter)
    val horizontalArrangement = Arrangement.Horizontal(horizontalGutter)

    val SMALL_IMAGE_ONLY =
        UserStack(Arrangement.Vertical(verticalGutter), mediumImageSize, NONE_TEXT)
    val SMALL_VERTICAL =
        UserStack(Arrangement.Vertical(verticalGutter), mediumImageSize, MEDIUM_TEXT)
    val LARGE_VERTICAL = UserStack(Arrangement.Vertical(verticalGutter), largeImageSize, LARGE_TEXT)
    val SMALL_HORIZONTAL =
        UserStack(Arrangement.Horizontal(horizontalGutter), mediumImageSize, MEDIUM_TEXT)
    val LARGE_HORIZONTAL =
        UserStack(Arrangement.Horizontal(horizontalGutter), largeImageSize, LARGE_TEXT)
}


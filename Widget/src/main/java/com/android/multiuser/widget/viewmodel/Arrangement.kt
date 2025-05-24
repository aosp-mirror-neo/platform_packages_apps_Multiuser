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
import androidx.compose.ui.unit.dp

sealed class Arrangement(val gutter: Dp = 0.dp, val padding: Dp = 0.dp) {
    class Horizontal(gutter: Dp = 8.dp, padding: Dp = 0.dp) : Arrangement(gutter, padding)
    class Vertical(gutter: Dp = 8.dp, padding: Dp = 0.dp)  : Arrangement(gutter, padding)
}
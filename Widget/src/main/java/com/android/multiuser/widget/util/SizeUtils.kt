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

package com.android.multiuser.widget.util

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


fun Resources.spToDp(@DimenRes dimenId: Int) = getDimension(dimenId).inSp(displayMetrics).value.dp
fun Resources.dp(@DimenRes dimenId: Int) = getDimension(dimenId).inDp(displayMetrics)
fun Resources.sp(@DimenRes dimenId: Int) = getDimension(dimenId).inSp(displayMetrics)

fun Float.inSp(metrics: DisplayMetrics) = TypedValue.deriveDimension(
    TypedValue.COMPLEX_UNIT_SP, this, metrics).sp

fun Float.inDp(metrics: DisplayMetrics) = TypedValue.deriveDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, metrics).dp

fun TextUnit.toDp(metrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics).dp



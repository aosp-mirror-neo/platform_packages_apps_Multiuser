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

package com.android.multiuser.stacks.ui

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Retrieves a dimension resource by its ID and converts it to [Dp].
 *
 * @param dimenId The resource ID of the dimension.
 * @return The dimension value as [Dp].
 */
fun Resources.dp(@DimenRes dimenId: Int) = getDimension(dimenId).inDp(displayMetrics)

/**
 * Retrieves a dimension resource by its ID and converts it to [TextUnit] (sp).
 *
 * @param dimenId The resource ID of the dimension.
 * @return The dimension value as [TextUnit] (sp).
 */
fun Resources.sp(@DimenRes dimenId: Int) = getDimension(dimenId).inSp(displayMetrics)

/**
 * Retrieves a dimension resource by its ID in raw pixels.
 *
 * @param dimenId The resource ID of the dimension.
 * @return The dimension value in pixels as a [Float].
 */
fun Resources.px(@DimenRes dimenId: Int) = getDimension(dimenId)

/**
 * Converts a [Float] value (assumed to be in pixels) to scalable pixels ([TextUnit] sp)
 * based on the provided [DisplayMetrics].
 *
 * @param metrics The [DisplayMetrics] to use for the conversion.
 * @return The value converted to [TextUnit] (sp).
 */
fun Float.inSp(metrics: DisplayMetrics) = TypedValue.deriveDimension(
    TypedValue.COMPLEX_UNIT_SP, this, metrics).sp

/**
 * Converts a [Float] value (assumed to be in pixels) to density-independent pixels ([Dp])
 * based on the provided [DisplayMetrics].
 *
 * @param metrics The [DisplayMetrics] to use for the conversion.
 * @return The value converted to [Dp].
 */
fun Float.inDp(metrics: DisplayMetrics) = TypedValue.deriveDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, metrics).dp

/**
 * Converts a [Dp] value to pixels based on the provided [DisplayMetrics].
 *
 * @param metrics The [DisplayMetrics] to use for the conversion.
 * @return The value in pixels as a [Float].
 */
fun Dp.toPx(metrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics)

/**
 * Converts a [TextUnit] value (sp) to pixels based on the provided [DisplayMetrics].
 *
 * @param metrics The [DisplayMetrics] to use for the conversion.
 * @return The value in pixels as a [Float].
 */
fun TextUnit.toPx(metrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics)


/**
 * Applies a size modifier to a [GlanceModifier] using a pixel value.
 * The pixel value is converted to [Dp] using the current context's display metrics.
 *
 * @param pixels The size in pixels.
 * @return A [GlanceModifier] with the specified size.
 */
@Composable
fun GlanceModifier.size(pixels: Float) =
    size(pixels.inDp(LocalContext.current.resources.displayMetrics))

/**
 * Applies a width modifier to a [GlanceModifier] using a pixel value.
 * The pixel value is converted to [Dp] using the current context's display metrics.
 *
 * @param pixels The width in pixels.
 * @return A [GlanceModifier] with the specified width.
 */
@Composable
fun GlanceModifier.width(pixels: Float) =
    width(pixels.inDp(LocalContext.current.resources.displayMetrics))

/**
 * Applies a height modifier to a [GlanceModifier] using a pixel value.
 * The pixel value is converted to [Dp] using the current context's display metrics.
 *
 * @param pixels The height in pixels.
 * @return A [GlanceModifier] with the specified height.
 */
@Composable
fun GlanceModifier.height(pixels: Float) =
    height(pixels.inDp(LocalContext.current.resources.displayMetrics))

/**
 * Applies padding to a [GlanceModifier] using horizontal and vertical pixel values.
 * The pixel values are converted to [Dp] using the current context's display metrics.
 *
 * @param horizontal The horizontal padding in pixels.
 * @param vertical The vertical padding in pixels.
 * @return A [GlanceModifier] with the specified padding.
 */
@Composable
fun GlanceModifier.padding(horizontal: Float, vertical: Float) =
    padding(
        horizontal = horizontal.inDp(LocalContext.current.resources.displayMetrics),
        vertical = vertical.inDp(LocalContext.current.resources.displayMetrics))

/**
 * Applies a corner radius to a [GlanceModifier] using a pixel value.
 * The pixel value is converted to [Dp] using the current context's display metrics.
 * This is specifically for use with Glance AppWidgets.
 *
 * @param pixels The corner radius in pixels.
 * @return A [GlanceModifier] with the specified corner radius.
 */
@Composable
fun GlanceModifier.cornerRadius(pixels: Float) =
    cornerRadius(pixels.inDp(LocalContext.current.resources.displayMetrics))

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

package com.android.multiuser.stacks

import kotlin.math.min
import kotlin.math.max

/**
 * Represents a dimension that can be resized within a minimum and maximum range.
 * This is typically used for layout calculations where components can have flexible sizes.
 *
 * @property min The minimum pixels allowed value for this dimension.
 * @property max The maximum pixels allowed value for this dimension.
 */
open class Resizable(var min : Float, var max : Float) {
    /**
     * If true, will attempt to stretch to fill all available pixels,
     * effectively setting its min and max to the available pixels.
     */
    var stretchToFill = false

    /**
     * Creates a new [Resizable] by adding the min and max values of another [Resizable]
     * to this one.
     * @param other The [Resizable] to add.
     * @return A new [Resizable] instance with the summed min and max values.
     */
    fun add(other: Resizable) = Resizable(min + other.min, max + other.max)

    /**
     * Creates a new [Resizable] by adding a float value to both the min and max
     * values of this one.
     * @param other The float value to add.
     * @return A new [Resizable] instance with the adjusted min and max values.
     */
    fun add(other: Float) = Resizable(min + other, max + other)

    /**
     * Creates a new [Resizable] by adding a float value to both the min and max
     * values of this one.
     * @param other The float value to add.
     * @return A new [Resizable] instance with the adjusted min and max values.
     */
    fun max(other: Resizable) = Resizable(
        max(min, other.min),
        max(max, other.max)
    )

    /**
     * Creates a new [Resizable] by multiplying both the min and max values of this one
     * by a float scalar.
     * @param other The float scalar to multiply by.
     * @return A new [Resizable] instance with the scaled min and max values.
     */
    fun times(other: Float) = Resizable(min * other, max * other)

    /**
     * Increases the [min] value of this [Resizable] if the [totalAvailable] space
     * is greater than the current [min]. The new [min] will not exceed [max].
     *
     * @param totalAvailable The total available pixels that this [Resizable] can occupy.
     * @return The (potentially updated) [min] value.
     */
    open fun increaseMin(totalAvailable: Float): Float {
        if(max >= totalAvailable) {
            max = totalAvailable
        }
        if (totalAvailable > min) {
            min = max
        }
        return min
    }

    /**
     * If [stretchToFill] is true, this method sets both [min] and [max] to [totalAvailable],
     * effectively making this [Resizable] occupy all the given pixels.
     *
     * @param totalAvailable The total available pixels to stretch into.
     * @return The (potentially updated) [min] value (which will be equal to [max] if stretched).
     */
    open fun stretchTo(totalAvailable: Float): Float {
        if (stretchToFill) {
            min = totalAvailable
            max = totalAvailable
        }
        return min
    }

    override fun toString(): String {
        val stretchable = if(stretchToFill) { "stretchable" } else { "non-stretchable" }
        return "$stretchable <${(min)}, ${(max)}>"
    }
}

/**
 * A specialized [Resizable] where the minimum and maximum values are fixed to the same value.
 * This represents a dimension with a constant, non-flexible size.
 *
 * @property v The fixed value for both the minimum and maximum dimension.
 */
class Fixed(val v: Float) : Resizable(v, v)
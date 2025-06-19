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

/**
 * Represents an entity with resizable dimensions in both horizontal and vertical axes.
 * This class encapsulates two [Resizable] instances, one for each dimension.
 *
 * @property horizontally The [Resizable] dimension for the horizontal axis.
 * @property vertically The [Resizable] dimension for the vertical axis.
 */
open class Measurable(val horizontally: Resizable, val vertically: Resizable) {
    /**
     * Creates a new [Measurable] by adding the dimensions of another [Measurable]
     * to this one.
     * @param other The [Measurable] whose dimensions are to be added.
     * @return A new [Measurable] instance with the summed dimensions.
     */
    fun add(other: Measurable) =
        Measurable(horizontally.add(other.horizontally),
            vertically.add(other.vertically))

    /**
     * Creates a new [Measurable] by adding a float value to both horizontal and
     * vertical dimensions of this one.
     * @param other The float value to add to each dimension.
     * @return A new [Measurable] instance with the adjusted dimensions.
     */
    fun add(other: Float) = Measurable(horizontally.add(other),
        vertically.add(other))

    /**
     * Creates a new [Measurable] by taking the maximum of the corresponding dimensions
     * between this [Measurable] and another.
     * @param other The [Measurable] to compare with.
     * @return A new [Measurable] instance representing the maximum bounds.
     */
    fun max(other: Measurable) =
        Measurable(horizontally.max(other.horizontally),
            vertically.max(other.vertically))

    /**
     * Increases the minimum vertical dimension if the [totalAvailable] space
     * is greater than the current minimum.
     * Delegates to [Resizable.increaseMin] for the vertical dimension.
     *
     * @param totalAvailable The total available vertical space.
     * @return The (potentially updated) minimum vertical dimension.
     */
    open fun increaseVerticalMin(totalAvailable: Float): Float {
        return vertically.increaseMin(totalAvailable)
    }

    /**
     * Stretches the vertical dimension to fill the [totalAvailable] space if its
     * [Resizable.stretchToFill] property is true.
     * Delegates to [Resizable.stretchTo] for the vertical dimension.
     *
     * @param totalAvailable The total available vertical space to stretch into.
     * @return The (potentially updated) minimum vertical dimension.
     */
    open fun stretchVertically(totalAvailable: Float): Float {
        return vertically.stretchTo(totalAvailable)
    }

    /**
     * Increases the minimum horizontal dimension if the [totalAvailable] space
     * is greater than the current minimum.
     * Delegates to [Resizable.increaseMin] for the horizontal dimension.
     *
     * @param totalAvailable The total available horizontal space.
     * @return The (potentially updated) minimum horizontal dimension.
     */
    open fun increaseHorizontalMin(totalAvailable: Float): Float {
        return horizontally.increaseMin(totalAvailable)
    }

    /**
     * Stretches the horizontal dimension to fill the [totalAvailable] space if its
     * [Resizable.stretchToFill] property is true.
     * Delegates to [Resizable.stretchTo] for the horizontal dimension.
     *
     * @param totalAvailable The total available horizontal space to stretch into.
     * @return The (potentially updated) minimum horizontal dimension.
     */
    open fun stretchHorizontally(totalAvailable: Float): Float {
        return horizontally.stretchTo(totalAvailable)
    }

    override fun toString(): String {
        return "($horizontally x $vertically)"
    }
}

/**
 * A specialized [Measurable] where both horizontal and vertical dimensions are defined
 * by the same [Resizable] instance, effectively creating a square that can resize.
 *
 * @param v The [Resizable] instance defining both width and height.
 */
open class ResizableSquare(v: Resizable) : Measurable(v, v)

/**
 * A specialized [ResizableSquare] where the dimensions are fixed.
 * This creates a square with a constant, non-flexible size.
 *
 * @param v The fixed value for both width and height.
 */
open class FixedSquare(v: Float) : ResizableSquare(Fixed(v))

/**
 * A specialized [Measurable] representing a rectangle with fixed width and height.
 *
 * @param w The fixed width of the rectangle.
 * @param h The fixed height of the rectangle.
 */
open class FixedRect(w: Float, h: Float)
    : Measurable(Fixed(w), Fixed(h))
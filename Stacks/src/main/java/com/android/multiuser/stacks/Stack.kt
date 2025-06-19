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

import androidx.glance.layout.Alignment
import kotlin.math.min
import kotlin.math.max

/**
 * Abstract base class for arranging a list of [Measurable] items either horizontally or vertically.
 * It calculates the overall dimensions of the stack based on its children, gutter, and padding.
 * Stack is also [Measurable] so list can contain nested stacks.
 *
 * @property gutter The spacing between individual items in the stack.
 * @property padding The padding around the entire stack of items.
 * @property list The list of [Measurable] items to be arranged in the stack.
 */
abstract class Stack(open var gutter: Float, open var padding: Measurable, val list: List<Measurable>)
    : Measurable(Resizable(0f, 0f), Resizable(0f, 0f)) {

    /** Tag for logging, defaults to the simple name of the class. */
    open var TAG: String = javaClass.simpleName

    /**
     * Total space occupied by gutters between items.
     * Calculated as `(list.size - 1) * gutter`.
     * Note: This can be negative if the list is empty.
     */
    open var gutterSpacing: Float = (list.size - 1) * gutter

    /** Total vertical padding (top and bottom). Calculated as `padding.vertically * 2`. */
    open var verticalPadding: Resizable = padding.vertically.times(2f)

    /** Total vertical space taken by padding and gutters. */
    open var verticalSpacing: Resizable = verticalPadding.add(gutterSpacing)

    /** Total horizontal padding (start and end). Calculated as `padding.horizontally * 2`. */
    open var horizontalPadding: Resizable = padding.horizontally.times(2f)

    /** Total horizontal space taken by padding and gutters. */
    open var horizontalSpacing: Resizable = horizontalPadding.add(gutterSpacing)

    /**
     * The sum of the dimensions of all items in the [list].
     * Note: This will throw an exception if the list is empty.
     */
    open var sumSize: Measurable = list.reduce { a, b -> a.add(b) }

    /**
     * The maximum dimensions among all items in the [list].
     * Note: This will throw an exception if the list is empty.
     */
    open var maxSize: Measurable = list.reduce { a, b -> a.max(b) }

    /** The alignment of items within the stack. Defaults to [Alignment.Center]. */
    open var alignment: Alignment = Alignment.Center


    init {
        recalculateHorizontal()
        recalculateVertical()
    }

    /**
     * Recalculates horizontal properties like [sumSize], [maxSize], [gutterSpacing],
     * [horizontalPadding], and [horizontalSpacing] based on the current [list],
     * [gutter], and [padding].
     * This should be called if these properties or the list's contents change.
     * Note: This method uses `reduce` which will throw an exception if `list` is empty.
     */
    open fun recalculateHorizontal() {
        sumSize = list.reduce{ a,b -> a.add(b) }
        maxSize = list.reduce{ a,b -> a.max(b) }
        gutterSpacing = (list.size-1) * gutter
        horizontalPadding = padding.horizontally.times(2f)
        horizontalSpacing = horizontalPadding.add(gutterSpacing)
    }

    /**
     * Recalculates vertical properties like [sumSize], [maxSize], [gutterSpacing],
     * [verticalPadding], and [verticalSpacing] based on the current [list],
     * [gutter], and [padding].
     * This should be called if these properties or the list's contents change.
     * Note: This method uses `reduce` which will throw an exception if `list` is empty.
     */
    open fun recalculateVertical() {
        sumSize = list.reduce{ a,b -> a.add(b) }
        maxSize = list.reduce{ a,b -> a.max(b) }
        gutterSpacing = (list.size-1) * gutter
        verticalPadding = padding.vertically.times(2f)
        verticalSpacing = verticalPadding.add(gutterSpacing)
    }

    /**
     * Adjusts the stack's dimensions based on the available width and height.
     * It attempts to increase the minimum dimensions of the stack and its children,
     * and then stretch them to fill the available space if they are configured to do so.
     *
     * @param width The total available width.
     * @param height The total available height.
     */
    open fun autoLayout(width: Float, height: Float){
        increaseHorizontalMin(width)
        increaseVerticalMin(height)
        stretchHorizontally(width)
        stretchVertically(height)
    }

    /**
     * A [Horizontal] stack with fixed gutter and horizontal padding. Vertical padding is zero.
     *
     * @param gutter The fixed spacing between items. Defaults to 0f.
     * @param padding The fixed horizontal padding for the stack. Defaults to 0f.
     * @param list The list of [Measurable] items.
     */
    open class HorizontalFixed(gutter: Float = 0f, padding: Float = 0f,  list: List<Measurable>)
        : Horizontal(gutter, Measurable(Fixed(padding), Fixed(0f)), list)

    /**
     * A [Vertical] stack with fixed gutter and vertical padding. Horizontal padding is zero.
     *
     * @param gutter The fixed spacing between items. Defaults to 0f.
     * @param padding The fixed vertical padding for the stack. Defaults to 0f.
     * @param list The list of [Measurable] items.
     */
    open class VerticalFixed(gutter: Float = 0f, padding: Float = 0f, list: List<Measurable>)
        : Vertical(gutter, Measurable(Fixed(0f), Fixed(padding)), list)

    /**
     * A [Stack] that arranges items horizontally.
     * The stack's width is the sum of its children's widths plus horizontal spacing (gutters and padding).
     * The stack's height is the maximum height of its children plus vertical padding.
     *
     * @param gutter Spacing between items. Defaults to 0f.
     * @param padding Padding around the stack. Defaults to a [FixedSquare] of 0f.
     * @param list The list of [Measurable] items.
     */
    open class Horizontal(gutter: Float = 0f, padding: Measurable = FixedSquare(0f),  list: List<Measurable>)
        : Stack(gutter, padding, list) {
        override var alignment = Alignment.CenterStart

        /**
         * Recalculates horizontal dimensions for the [Horizontal] stack.
         * Sets the stack's horizontal min/max to the sum of children's horizontal dimensions
         * plus [horizontalSpacing] (gutters and padding).
         */
        override fun recalculateHorizontal() {
            super.recalculateHorizontal()
            horizontalPadding = padding.horizontally.times(2f)
            horizontalSpacing = horizontalPadding.add(gutterSpacing)
            sumSize.horizontally.add(horizontalSpacing).let {
                horizontally.min = it.min
                horizontally.max = it.max
            }
        }

        /**
         * Increases the minimum horizontal dimension of the stack and its children.
         * If `totalAvailable` width is greater than the current stack minimum width,
         * the additional space is distributed proportionally among children that can grow
         * (i.e., their `max` width is greater than their `min` width).
         *
         * @param totalAvailable The total available width.
         * @return The new minimum horizontal dimension of the stack.
         */
        override fun increaseHorizontalMin(totalAvailable: Float) : Float {
            // for proportional increase we do not count spacing as it is taken away in horizontally min
            var difference = totalAvailable - horizontally.min
            if (difference > 0) {
                val items = list.filter { it.horizontally.max - it.horizontally.min > 0 }
                val proportional = difference / items.size
                items.forEach {
                    it.increaseHorizontalMin(proportional + it.horizontally.min)
                }
            }
            recalculateHorizontal()
            horizontally.increaseMin(totalAvailable)
            return horizontally.min
        }

        /**
         * Stretches the horizontal dimension of the stack and its stretchable children
         * to fill the `totalAvailable` width.
         * If the stack itself is set to `horizontally.stretchToFill`, it will attempt to
         * distribute any space beyond its current minimum width (after `increaseHorizontalMin`)
         * among its children that have `horizontally.stretchToFill` set to true.
         *
         * @param totalAvailable The total available width to stretch into.
         * @return The new minimum horizontal dimension of the stack (which will be its stretched size).
         */
        override fun stretchHorizontally(totalAvailable: Float) : Float {
            // for proportional increase we do not count spacing as it is taken away in horizontally min
            var difference = totalAvailable - horizontally.min
            if(difference > 0) {
                val items = list.filter { it.horizontally.stretchToFill }
                val proportional = difference / items.size
                items.forEach { item ->
                    item.stretchHorizontally(proportional + item.horizontally.min)
                }
            }
            recalculateHorizontal()
            horizontally.stretchTo(totalAvailable)
            return horizontally.min
        }


        /**
         * Recalculates vertical dimensions for the [Horizontal] stack.
         * Sets the stack's vertical min/max to the maximum of children's vertical dimensions
         * plus [verticalPadding]. Gutter is not included as items are arranged horizontally.
         */
        override fun recalculateVertical() {
            super.recalculateVertical()
            verticalPadding = padding.vertically.times(2f)
            verticalSpacing = verticalPadding
            maxSize.vertically.add(verticalPadding).let {
               vertically.min = it.min
               vertically.max = it.max
            }
        }

     /**
         * Increases the minimum vertical dimension of the stack and its children.
         * Each child that can grow vertically is asked to increase its minimum height
         * to fill the available content height within the stack (stack's target height minus padding).
         *
         * @param totalAvailable The total available height for the stack.
         * @return The new minimum vertical dimension of the stack.
         */
        override fun increaseVerticalMin(totalAvailable: Float): Float {
            val items = list.filter { it.vertically.max - it.vertically.min > 0 }
            val availableMin = totalAvailable - verticalSpacing.min
            if(availableMin > 0) {
                this.vertically.min =
                    max(vertically.min, min(this.vertically.max, totalAvailable))
                items.forEach { it.increaseVerticalMin(this.vertically.min - verticalSpacing.min) }
            }
            recalculateVertical()
            vertically.increaseMin(totalAvailable)
            return vertically.min
        }


        /**
         * Stretches the vertical dimension of the stack and its children
         * to fill the `totalAvailable` height.
         * If the stack itself is set to `vertically.stretchToFill`, it will attempt to make
         * its children (especially those also set to `vertically.stretchToFill`)
         * occupy the available content height within the stack.
         *
         * @param totalAvailable The total available height to stretch into.
         * @return The new minimum vertical dimension of the stack (which will be its stretched size).
         */
        override fun stretchVertically(totalAvailable: Float): Float {
            if (vertically.stretchToFill) { // If the stack itself is supposed to stretch
                // Calculate the height available for the children content area
                val heightForChildrenContent = totalAvailable - verticalSpacing.min // verticalSpacing is padding for Horizontal

                if (heightForChildrenContent > 0) {
                    val itemsToProcess = list.filter { it.vertically.stretchToFill }

                    itemsToProcess.forEach { item ->
                        item.stretchVertically(heightForChildrenContent)
                    }
                }

                recalculateVertical()

                // Finally, ensure the stack itself honors the totalAvailable height if it's set to stretch.
                // This call will set the stack's min and max height to totalAvailable.
                vertically.stretchTo(totalAvailable)
            }
            return vertically.min
        }

        override fun toString(): String {
            return "\n\nH $TAG" +
                    "($horizontally x $vertically)" +
                    "\ngutter=${(gutter).toInt()} padding=$padding" +
                    "\nlist=${list.joinToString ("\n\t" )})"
        }
    }

    /**
     * A [Stack] that arranges items vertically.
     * The stack's height is the sum of its children's heights plus vertical spacing (gutters and padding).
     * The stack's width is the maximum width of its children plus horizontal padding.
     *
     * @param gutter Spacing between items. Defaults to 0f.
     * @param padding Padding around the stack. Defaults to a [FixedSquare] of 0f.
     * @param list The list of [Measurable] items.
     */
    open class Vertical(gutter: Float = 0f, padding: Measurable= FixedSquare(0f), list: List<Measurable>)
        : Stack(gutter, padding, list) {
        override var alignment = Alignment.TopCenter

        /**
         * Recalculates horizontal dimensions for the [Vertical] stack.
         * Sets the stack's horizontal min/max to the maximum of children's horizontal dimensions
         * plus [horizontalPadding]. Gutter is not included as items are arranged vertically.
         */
        override fun recalculateHorizontal() {
            super.recalculateHorizontal()
            horizontalPadding = padding.horizontally.times(2f)
            horizontalSpacing = horizontalPadding
            maxSize.horizontally.add(horizontalPadding).let {
                horizontally.min = it.min
                horizontally.max = it.max
            }
        }

        /**
         * Recalculates vertical dimensions for the [Vertical] stack.
         * Sets the stack's vertical min/max to the sum of children's vertical dimensions
         * plus [verticalSpacing] (gutters and padding).
         */
        override fun recalculateVertical() {
            super.recalculateVertical()
            verticalPadding = padding.vertically.times(2f)
            verticalSpacing = verticalPadding.add(gutterSpacing)
            sumSize.vertically.add(verticalSpacing).let {
                vertically.min = it.min
                vertically.max = it.max
            }
        }

        /**
         * Increases the minimum vertical dimension of the stack and its children.
         * If `totalAvailable` height is greater than the current stack minimum height,
         * the stack's own min height is updated (capped by its max). The additional
         * space (`difference`) is then distributed proportionally among children that can grow.
         *
         * @param totalAvailable The total available height.
         * @return The new minimum vertical dimension of the stack.
         */
        override fun increaseVerticalMin(totalAvailable: Float) : Float {
            val difference = totalAvailable - vertically.min
            if(difference > 0) {
                vertically.min = min(vertically.max, totalAvailable)
                val items = list.filter { it.vertically.max - it.vertically.min > 0 }
                val proportional = difference / items.size
                items.forEach {
                    it.increaseVerticalMin(proportional + it.vertically.min - verticalSpacing.min)
                }
            }
            recalculateVertical()
            vertically.increaseMin(totalAvailable)
            return vertically.min
        }

      /**
         * Stretches the vertical dimension of the stack and its stretchable children
         * to fill the `totalAvailable` height.
         * If the stack itself is set to `vertically.stretchToFill`, it will attempt to
         * distribute any space beyond its current minimum height (after `increaseVerticalMin`)
         * among its children that have `vertically.stretchToFill` set to true.
         *
         * @param totalAvailable The total available height to stretch into.
         * @return The new minimum vertical dimension of the stack (which will be its stretched size).
         */
        override fun stretchVertically(totalAvailable: Float) : Float {
            if(vertically.stretchToFill) {
                val difference = totalAvailable - vertically.min
                if (difference > 0) {
                    vertically.min = min(vertically.max, totalAvailable)
                    val items = list.filter { it.vertically.stretchToFill }
                    val proportional = difference / items.size
                    items.forEach { item ->
                        item.stretchVertically(proportional + item.vertically.min)
                    }
                }
                recalculateVertical()
                vertically.stretchTo(totalAvailable)
            }
            return vertically.min
        }

        /**
         * Increases the minimum horizontal dimension of the stack and its children.
         * Each child that can grow horizontally is asked to increase its minimum width
         * to fill the available content width within the stack (stack's target width minus padding).
         *
         * @param totalAvailable The total available width for the stack.
         * @return The new minimum horizontal dimension of the stack.
         */
        override fun increaseHorizontalMin(totalAvailable: Float) : Float {
            val items = list.filter { it.horizontally.max - it.horizontally.min > 0 }
            var availableMin = min(this.horizontally.max - horizontalPadding.min, totalAvailable - horizontalPadding.min)

            if(availableMin > 0) {
                items.forEach { it.increaseHorizontalMin(availableMin) }
            }
            recalculateHorizontal()
            horizontally.increaseMin(totalAvailable)
            return horizontally.min
        }

        /**
         * Stretches the horizontal dimension of the stack and its children
         * to fill the `totalAvailable` width.
         * If the stack itself is set to `horizontally.stretchToFill`, it will attempt to make
         * its children (especially those also set to `horizontally.stretchToFill`)
         * occupy the available content width within the stack.
         *
         * @param totalAvailable The total available width to stretch into.
         * @return The new minimum horizontal dimension of the stack (which will be its stretched size).
         */
        override fun stretchHorizontally(totalAvailable: Float): Float {
            val items = list.filter { it.horizontally.stretchToFill }
            var availableMin = totalAvailable - horizontalPadding.min

            if(availableMin > 0) {
                items.forEach { it.stretchHorizontally(availableMin) }
            }
            recalculateHorizontal()
            horizontally.stretchTo(totalAvailable)
            return horizontally.min
        }

        override fun toString(): String {
            return "\n\nV $TAG" +
                    "($horizontally x $vertically)" +
                    "\ngutter=${(gutter).toInt()} padding=$padding" +
                    "\nlist=${list.joinToString ("\n\t" )})"
        }
    }
}

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

package com.android.multiuser.stacks.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import com.android.multiuser.stacks.*

@RunWith(AndroidJUnit4::class)
class StackTest {

    // Class-level variables for common dimensions
    private val smallImageMin = 10f
    private val smallImageMax = 20f
    private val largeImageMin = 12f
    private val largeImageMax = 24f

    private val smallImageWidthMin = 10f
    private val smallImageWidthMax = 15f
    private val smallImageHeightMin = 20f
    private val smallImageHeightMax = 25f

    private val largeImageWidthMin = 20f
    private val largeImageWidthMax = 25f
    private val largeImageHeightMin = 30f
    private val largeImageHeightMax = 35f

    private val gutter = 8f
    private val padding = 16f
    private val fixedSquarePadding = FixedSquare(padding)

    private var smallImageWidth = Resizable(smallImageWidthMin, smallImageWidthMax)
    private var smallImageHeight = Resizable(smallImageHeightMin, smallImageHeightMax)
    private var largeImageWidth = Resizable(largeImageWidthMin, largeImageWidthMax)
    private var largeImageHeight = Resizable(largeImageHeightMin, largeImageHeightMax)

    private var smallImage = Measurable(smallImageWidth, smallImageHeight)
    private var largeImage = Measurable(largeImageWidth, largeImageHeight)


    @Before
    fun resetDefaults() {
        smallImageWidth = Resizable(smallImageWidthMin, smallImageWidthMax)
        smallImageHeight = Resizable(smallImageHeightMin, smallImageHeightMax)
        largeImageWidth = Resizable(largeImageWidthMin, largeImageWidthMax)
        largeImageHeight = Resizable(largeImageHeightMin, largeImageHeightMax)

        smallImage = Measurable(smallImageWidth, smallImageHeight)
        largeImage = Measurable(largeImageWidth, largeImageHeight)
    }

    // Given: Two square Measurable items (smallImageSquare, largeImageSquare) with defined
    // Resizable dimensions.
    // When: A Vertical stack and a Horizontal stack are created with these items,
    // a specified gutter, and fixed square padding.
    // Then: The calculated vertical and horizontal min/max dimensions of both stacks should
    // correctly reflect the sum of item dimensions, gutter, and padding for the main axis,
    // and the max of item dimensions plus padding for the cross axis.
    @Test
    fun squareImagesStacksTest() {
        val smallImageSize = Resizable(smallImageMin, smallImageMax)
        val largeImageSize = Resizable(largeImageMin, largeImageMax)
        val smallImageSquare = Measurable(smallImageSize, smallImageSize)
        val largeImageSquare = Measurable(largeImageSize, largeImageSize)
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImageSquare, largeImageSquare)
        )
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImageSquare, largeImageSquare)
        )

        val minSum = padding + smallImageMin + gutter + largeImageMin + padding
        val maxSum = padding + smallImageMax + gutter + largeImageMax + padding
        assertEquals(minSum, verticalStack.vertically.min)
        assertEquals(maxSum, verticalStack.vertically.max)
        assertEquals(minSum, horizontalStack.horizontally.min)
        assertEquals(maxSum, horizontalStack.horizontally.max)

        // Max of children's cross-axis size + padding
        val minCrossAxis = padding + largeImageMin + padding
        val maxCrossAxis = padding + largeImageMax + padding
        assertEquals(minCrossAxis, verticalStack.horizontally.min)
        assertEquals(maxCrossAxis, verticalStack.horizontally.max)
        assertEquals(minCrossAxis, horizontalStack.vertically.min)
        assertEquals(maxCrossAxis, horizontalStack.vertically.max)
    }

    // Given: Two rectangular Measurable items (smallImage, largeImage) with distinct width
    // and height Resizable dimensions.
    // When: A Vertical stack and a Horizontal stack are created with these items,
    // a specified gutter, and fixed square padding.
    // Then: The calculated min/max dimensions of both stacks should be correct:
    //        - For the main axis (vertical for VerticalStack, horizontal for HorizontalStack):
    //          sum of item dimensions in that axis + gutter + padding.
    //        - For the cross axis: max of item dimensions in that axis + padding.
    @Test
    fun rectImagesStacksTest() {
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        val minWidthSum = padding + smallImageWidthMin + gutter + largeImageWidthMin + padding
        val maxWidthSum = padding + smallImageWidthMax + gutter + largeImageWidthMax + padding
        val minHeightSum = padding + smallImageHeightMin + gutter + largeImageHeightMin + padding
        val maxHeightSum = padding + smallImageHeightMax + gutter + largeImageHeightMax + padding
        assertEquals(minWidthSum, horizontalStack.horizontally.min)
        assertEquals(maxWidthSum, horizontalStack.horizontally.max)
        assertEquals(minHeightSum, verticalStack.vertically.min)
        assertEquals(maxHeightSum, verticalStack.vertically.max)

        val minWidthCross =
            padding + kotlin.math.max(smallImageWidthMin, largeImageWidthMin) + padding
        val maxWidthCross =
            padding + kotlin.math.max(smallImageWidthMax, largeImageWidthMax) + padding
        val minHeightCross =
            padding + kotlin.math.max(smallImageHeightMin, largeImageHeightMin) + padding
        val maxHeightCross =
            padding + kotlin.math.max(smallImageHeightMax, largeImageHeightMax) + padding

        assertEquals(minWidthCross, verticalStack.horizontally.min)
        assertEquals(maxWidthCross, verticalStack.horizontally.max)
        assertEquals(minHeightCross, horizontalStack.vertically.min)
        assertEquals(maxHeightCross, horizontalStack.vertically.max)
    }

    // Given: A Horizontal stack and a Vertical stack, each containing two non-stretchable
    // rectangular images.
    // When: increaseVerticalMin is called on both stacks with an amount (200f) that is
    // larger than the max height of the children.
    // Then: The vertical min and max dimensions of each child item in both stacks should be
    // capped at their original maxHeight.
    // And: The vertical min dimension of the Horizontal stack should be the sum of padding
    // and the maximum of its children's capped max heights.
    // And: The vertical min dimension of the Vertical stack should be the sum of padding,
    // gutter, and its children's capped max heights.
    @Test
    fun IncreaseVerticalWhenIncreaseIsBiggerThanVerticalTest() {
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        val increaseAmount = 200f
        horizontalStack.increaseVerticalMin(increaseAmount)
        verticalStack.increaseVerticalMin(increaseAmount)

        // Children should be capped at their max heights
        assertEquals(smallImageHeightMax, horizontalStack.list.get(0).vertically.min)
        assertEquals(smallImageHeightMax, horizontalStack.list.get(0).vertically.max)
        assertEquals(largeImageHeightMax, horizontalStack.list.get(1).vertically.min)
        assertEquals(largeImageHeightMax, horizontalStack.list.get(1).vertically.max)

        assertEquals(smallImageHeightMax, verticalStack.list.get(0).vertically.min)
        assertEquals(smallImageHeightMax, verticalStack.list.get(0).vertically.max)
        assertEquals(largeImageHeightMax, verticalStack.list.get(1).vertically.min)
        assertEquals(largeImageHeightMax, verticalStack.list.get(1).vertically.max)

        // Stack dimensions
        val expectedHStackVertMin =
            padding + kotlin.math.max(smallImageHeightMax, largeImageHeightMax) + padding
        val expectedVStackVertMin =
            padding + smallImageHeightMax + gutter + largeImageHeightMax + padding

        assertEquals(expectedHStackVertMin, horizontalStack.vertically.min)
        assertEquals(expectedVStackVertMin, verticalStack.vertically.min)
    }

    // Given: A Horizontal stack and a Vertical stack, each containing two non-stretchable
    // rectangular images.
    // When: increaseHorizontalMin is called on both stacks with an amount (200f) that is
    // larger than the max width of the children.
    // Then: The horizontal min and max dimensions of each child item in both stacks should be
    // capped at their original maxWidth.
    // And: The horizontal min dimension of the Horizontal stack should be the sum of padding,
    // gutter, and its children's capped max widths.
    // And: The horizontal min dimension of the Vertical stack should be the sum of padding
    // and the maximum of its children's capped max widths.
    @Test
    fun IncreaseHorizontalWhenIncreaseIsBiggerThanHorizontalTest() {
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        val increaseAmount = 200f
        horizontalStack.increaseHorizontalMin(increaseAmount)
        verticalStack.increaseHorizontalMin(increaseAmount)

        // Children should be capped at their max widths
        assertEquals(smallImageWidthMax, horizontalStack.list.get(0).horizontally.min)
        assertEquals(smallImageWidthMax, horizontalStack.list.get(0).horizontally.max)
        assertEquals(largeImageWidthMax, horizontalStack.list.get(1).horizontally.min)
        assertEquals(largeImageWidthMax, horizontalStack.list.get(1).horizontally.max)

        assertEquals(smallImageWidthMax, verticalStack.list.get(0).horizontally.min)
        assertEquals(smallImageWidthMax, verticalStack.list.get(0).horizontally.max)
        assertEquals(largeImageWidthMax, verticalStack.list.get(1).horizontally.min)
        assertEquals(largeImageWidthMax, verticalStack.list.get(1).horizontally.max)

        // Stack dimensions
        val expectedHStackHorizMin =
            padding + smallImageWidthMax + gutter + largeImageWidthMax + padding
        val expectedVStackHorizMin =
            padding + kotlin.math.max(smallImageWidthMax, largeImageWidthMax) + padding

        assertEquals(expectedHStackHorizMin, horizontalStack.horizontally.min)
        assertEquals(expectedVStackHorizMin, verticalStack.horizontally.min)
    }

    // Given: A Vertical stack configured with `vertically.stretchToFill = true`,
    // containing one vertically stretchable smallImage and one non-stretchable largeImage.
    // When: `stretchVertically` is called on the stack with a targetHeight of 200f.
    // Then: The stack's vertical min and max dimensions should both be equal to targetHeight.
    // And: The stack's verticalSpacing should be the sum of top padding, gutter, and bottom padding.
    // And: The stack's horizontal (cross-axis) min dimension should be the sum of padding
    // and the maximum of its children's initial widths.
    // And: The stretchable smallImage's vertical min and max should be the targetHeight
    // minus stack's verticalSpacing and the largeImage's min height.
    // And: The non-stretchable largeImage's vertical min should remain its initial min height,
    // and its max should remain its initial max height.
    @Test
    fun StretchVerticalWhenVerticalIsStretchableVStackTest() {
        val smallImageHeight = smallImageHeight.apply { stretchToFill = true }
        val smallImage = Measurable(smallImageWidth, smallImageHeight)
            .apply { vertically.stretchToFill = true }
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        ).apply { vertically.stretchToFill = true }

        val targetHeight = 200f
        verticalStack.stretchVertically(targetHeight)

        assertEquals(targetHeight, verticalStack.vertically.min)
        assertEquals(targetHeight, verticalStack.vertically.max)
        assertEquals(padding + gutter + padding, verticalStack.verticalSpacing.min) // p + g + p

        // Cross-axis (horizontal)
        val expectedStackWidth =
            padding + kotlin.math.max(smallImageWidthMin, largeImageWidthMin) + padding
        assertEquals(expectedStackWidth, verticalStack.horizontally.min)
        assertEquals(padding + padding, verticalStack.horizontalSpacing.min) // p + p

        // largeImage is not stretchable, so it takes its min height.
        // smallImage takes the rest.
        val spaceForChildren = targetHeight - verticalStack.verticalSpacing.min
        val expectedSmallImageHeight = spaceForChildren - largeImageHeightMin
        assertEquals(expectedSmallImageHeight, verticalStack.list.get(0).vertically.min)
        assertEquals(expectedSmallImageHeight, verticalStack.list.get(0).vertically.max)
        assertEquals(largeImageHeightMin, verticalStack.list.get(1).vertically.min)
        assertEquals(largeImageHeightMax, verticalStack.list.get(1).vertically.max) // Max remains
    }

    // Given: A Horizontal stack configured with `vertically.stretchToFill = true`
    // (cross-axis stretch), containing one vertically stretchable smallImage and one
    // non-stretchable largeImage.
    // When: `stretchVertically` is called on the stack with a targetHeight of 200f.
    // Then: The stack's vertical min and max dimensions should both be equal to targetHeight.
    // And: The stack's verticalSpacing (padding only for Horizontal stack) should be the
    // sum of top and bottom padding.
    // And: The stack's horizontal (main-axis) min dimension should be the sum of padding,
    // gutter, and its children's initial widths.
    // And: The stretchable smallImage's vertical min and max should be the targetHeight
    // minus the stack's verticalSpacing.
    // And: The non-stretchable largeImage's vertical min should remain its initial min height,
    // and its max should remain its initial max height.
    @Test
    fun StretchVerticalWhenVerticalIsStretchableHStackTest() {
        smallImageHeight.apply { stretchToFill = true }
        val smallImage = Measurable(smallImageWidth, smallImageHeight)
            .apply { vertically.stretchToFill = true }
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        ).apply { vertically.stretchToFill = true }

        val targetHeight = 200f
        horizontalStack.stretchVertically(targetHeight)

        assertEquals(targetHeight, horizontalStack.vertically.min)
        assertEquals(targetHeight, horizontalStack.vertically.max)
        assertEquals(padding + padding, horizontalStack.verticalSpacing.min)

        // Main axis (horizontal)
        val expectedStackWidth =
            padding + smallImageWidthMin + gutter + largeImageWidthMin + padding
        assertEquals(expectedStackWidth, horizontalStack.horizontally.min)
        assertEquals(padding + gutter + padding, horizontalStack.horizontalSpacing.min)

        // Both children will be stretched to targetHeight - vertical padding
        val heightForChildren = targetHeight - horizontalStack.verticalSpacing.min
        assertEquals(heightForChildren, horizontalStack.list.first().vertically.min) // smallImage
        assertEquals(heightForChildren, horizontalStack.list.first().vertically.max)

        // the non-stretchable largeImage
        assertEquals(largeImageHeightMin, horizontalStack.list.last().vertically.min)
        assertEquals(largeImageHeightMax, horizontalStack.list.last().vertically.max)
    }

    // Given: A Horizontal stack and a Vertical stack, both initialized with default
    // non-stretchable behavior (`stretchToFill = false`).
    // When: `stretchVertically` is called on both stacks with an arbitrary target height (200f).
    // Then: The vertical dimensions (min and max) of both stacks should remain unchanged
    // from their initial calculated values.
    // And: The vertical dimensions of the children within these stacks should also remain unchanged.
    @Test
    fun NotStretchVerticalWhenVerticalIsNotStretchableTest() {
        // Stacks are not stretchToFill by default
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        val originalHStackVertMin = horizontalStack.vertically.min
        val originalVStackVertMin = verticalStack.vertically.min

        horizontalStack.stretchVertically(200f)
        verticalStack.stretchVertically(200f)

        // Dimensions should not change as stacks are not stretchable
        assertEquals(originalHStackVertMin, horizontalStack.vertically.min)
        assertEquals(originalVStackVertMin, verticalStack.vertically.min)

        // Children dimensions also shouldn't change
        assertEquals(smallImageHeightMin, horizontalStack.list.first().vertically.min)
        assertEquals(largeImageHeightMin, verticalStack.list.last().vertically.min)
    }

    // --- Nested Stack Tests ---

    // Given: A Horizontal stack (horizontalInnerStack) containing smallImage and largeImage.
    // And: A Vertical stack (verticalOuterStack) containing the horizontalInnerStack
    // and another smallImage.
    // When: The verticalOuterStack is initialized.
    // Then: The verticalOuterStack's vertical min/max dimensions should be the sum of its
    // children's vertical dimensions (horizontalInnerStack's height and smallImage's height)
    // plus outer padding and gutter.
    // And: The verticalOuterStack's horizontal min/max dimensions should be the sum of
    // outer padding and the maximum of its children's horizontal dimensions
    // (horizontalInnerStack's width and smallImage's width).
    @Test
    fun nestedHorizontalInVerticalStackTest() {
        val horizontalInnerStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )
        val verticalOuterStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(horizontalInnerStack, smallImage)
        )

        // Outer Vertical Stack - Vertical Dimension
        val expectedOuterVertMin =
            padding + horizontalInnerStack.vertically.min +
                    gutter + smallImage.vertically.min + padding
        val expectedOuterVertMax =
            padding + horizontalInnerStack.vertically.max +
                    gutter + smallImage.vertically.max + padding
        assertEquals(expectedOuterVertMin, verticalOuterStack.vertically.min)
        assertEquals(expectedOuterVertMax, verticalOuterStack.vertically.max)

        // Outer Vertical Stack - Horizontal Dimension (cross-axis)
        val expectedOuterHorizMin =
            padding + kotlin.math.max(
                horizontalInnerStack.horizontally.min,
                smallImage.horizontally.min
            ) + padding
        val expectedOuterHorizMax =
            padding + kotlin.math.max(
                horizontalInnerStack.horizontally.max,
                smallImage.horizontally.max
            ) + padding
        assertEquals(expectedOuterHorizMin, verticalOuterStack.horizontally.min)
        assertEquals(expectedOuterHorizMax, verticalOuterStack.horizontally.max)
    }

    // Given: A Vertical stack (verticalInnerStack) containing smallImage and largeImage.
    // And: A Horizontal stack (horizontalOuterStack) containing the verticalInnerStack
    // and another smallImage.
    // When: The horizontalOuterStack is initialized.
    // Then: The horizontalOuterStack's horizontal min/max dimensions should be the sum of its
    // children's horizontal dimensions (verticalInnerStack's width and smallImage's width)
    // plus outer padding and gutter.
    // And: The horizontalOuterStack's vertical min/max dimensions should be the sum of
    // outer padding and the maximum of its children's vertical dimensions
    // (verticalInnerStack's height and smallImage's height).
    @Test
    fun nestedVerticalInHorizontalStackTest() {
        val verticalInnerStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )
        val horizontalOuterStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(verticalInnerStack, smallImage)
        )

        // Outer Horizontal Stack - Horizontal Dimension
        val expectedOuterHorizMin =
            padding + verticalInnerStack.horizontally.min +
                    gutter + smallImage.horizontally.min + padding
        val expectedOuterHorizMax =
            padding + verticalInnerStack.horizontally.max +
                    gutter + smallImage.horizontally.max + padding
        assertEquals(expectedOuterHorizMin, horizontalOuterStack.horizontally.min)
        assertEquals(expectedOuterHorizMax, horizontalOuterStack.horizontally.max)

        // Outer Horizontal Stack - Vertical Dimension (cross-axis)
        val expectedOuterVertMin =
            padding + kotlin.math.max(
                verticalInnerStack.vertically.min,
                smallImage.vertically.min
            ) + padding
        val expectedOuterVertMax =
            padding + kotlin.math.max(
                verticalInnerStack.vertically.max,
                smallImage.vertically.max
            ) + padding
        assertEquals(expectedOuterVertMin, horizontalOuterStack.vertically.min)
        assertEquals(expectedOuterVertMax, horizontalOuterStack.vertically.max)
    }

    // Given: A vertically stretchable Horizontal inner stack (containing one vertically
    // stretchable smallImage and one non-stretchable largeImage).
    // And: A vertically stretchable Vertical outer stack containing the inner Horizontal
    // stack and a non-stretchable smallImage.
    // When: `stretchVertically` is called on the outer Vertical stack with a targetHeight of 300f.
    // Then: The outer Vertical stack's vertical min dimension should be equal to targetHeight.
    // And: The inner Horizontal stack's vertical min dimension should be the space
    // remaining in the outer stack after accounting for the outer stack's vertical spacing
    // and the non-stretchable smallImage's height.
    // And: The stretchable smallImage within the inner Horizontal stack should have its
    // vertical min dimension take up the space remaining in the inner stack after
    // accounting for the inner stack's vertical padding.
    // And: The non-stretchable largeImage within the inner Horizontal stack should
    // maintain its original min/max vertical dimensions.
    @Test
    fun stretchNestedHorizontalInVerticalStackTest() {
        // smallImage in horizontalInnerStack will be stretchable vertically
        val stretchableSmallImageHeight =
            Resizable(smallImageHeightMin, smallImageHeightMax).apply { stretchToFill = true }
        val stretchableSmallImage =
            Measurable(smallImageWidth, stretchableSmallImageHeight)
                .apply { vertically.stretchToFill = true }

        // horizontalInnerStack itself is stretchable vertically
        val horizontalInnerStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(stretchableSmallImage, largeImage)
        ).apply { vertically.stretchToFill = true }

        // verticalOuterStack is stretchable vertically
        val verticalOuterStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(horizontalInnerStack, smallImage)
        ).apply { vertically.stretchToFill = true }

        val targetHeight = 300f
        verticalOuterStack.stretchVertically(targetHeight)

        assertEquals(targetHeight, verticalOuterStack.vertically.min)

        // Calculate expected heights after stretching
        // Space for children in verticalOuterStack =
        // targetHeight - outerPadding - outerGutter - outerPadding
        val outerVerticalSpacing = padding + gutter + padding
        val spaceForOuterChildren = targetHeight - outerVerticalSpacing

        // smallImage (not stretchable) takes its min height
        // horizontalInnerStack (stretchable) takes the rest
        val expectedInnerStackHeight = spaceForOuterChildren - smallImage.vertically.min
        assertEquals(
            expectedInnerStackHeight,
            (verticalOuterStack.list.first() as Stack).vertically.min
        )

        // Now, for the horizontalInnerStack, its children (stretchableSmallImage, largeImage)
        // share 'expectedInnerStackHeight'
        // Space for children in horizontalInnerStack =
        // expectedInnerStackHeight - innerPadding - innerPadding
        val innerVerticalPadding = padding + padding
        val spaceForInnerChildren = expectedInnerStackHeight - innerVerticalPadding

        // stretchableSmallImage (stretchable) takes this space
        // largeImage (not stretchable) is capped by its max
        assertEquals(
            spaceForInnerChildren,
            (horizontalInnerStack.list.first() as Measurable).vertically.min
        )
        // Corrected assertions for the non-stretchable largeImage:
        // smallImage (not stretchable)
        assertEquals(
            largeImageHeightMin,
            (horizontalInnerStack.list.last() as Measurable).vertically.min
        )
        // smallImage (not stretchable)
        assertEquals(
            largeImageHeightMax,
            (horizontalInnerStack.list.last() as Measurable).vertically.max
        )
    }

    // Given: An inner Horizontal stack containing a smallImage and a largeImage.
    // And: An outer Horizontal stack containing the inner Horizontal stack and another smallImage.
    // When: The outer Horizontal stack is initialized.
    // Then: The outer stack's horizontal min/max (main axis) should be the sum of its
    // children's horizontal dimensions (inner stack width + smallImage width)
    // plus outer padding and gutter.
    // And: The outer stack's vertical min/max (cross axis) should be the sum of outer
    // padding and the maximum of its children's vertical dimensions
    // (max of inner stack height and smallImage height).
    @Test
    fun nestedHorizontalInHorizontalStackTest() {
        // Define the inner horizontal stack
        val innerHorizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        // Define the outer horizontal stack containing the inner stack and smallImage
        val outerHorizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(innerHorizontalStack, smallImage)
        )

        // --- Assertions for the Outer Horizontal Stack ---

        // Horizontal Dimension (Main Axis)
        val expectedOuterHorizMin =
            padding + innerHorizontalStack.horizontally.min +
                    gutter + smallImage.horizontally.min + padding
        val expectedOuterHorizMax =
            padding + innerHorizontalStack.horizontally.max +
                    gutter + smallImage.horizontally.max + padding
        assertEquals(
            "OuterHStack Horiz Min",
            expectedOuterHorizMin,
            outerHorizontalStack.horizontally.min
        )
        assertEquals(
            "OuterHStack Horiz Max",
            expectedOuterHorizMax,
            outerHorizontalStack.horizontally.max
        )

        // Vertical Dimension (Cross Axis)
        // The height of the outer horizontal stack is determined by the tallest child
        // (either the inner stack or smallImage) plus padding.
        val expectedOuterVertMin =
            padding + kotlin.math.max(
                innerHorizontalStack.vertically.min,
                smallImage.vertically.min
            ) + padding
        val expectedOuterVertMax =
            padding + kotlin.math.max(
                innerHorizontalStack.vertically.max,
                smallImage.vertically.max
            ) + padding
        assertEquals(
            "OuterHStack Vert Min",
            expectedOuterVertMin,
            outerHorizontalStack.vertically.min
        )
        assertEquals(
            "OuterHStack Vert Max",
            expectedOuterVertMax,
            outerHorizontalStack.vertically.max
        )
    }

    // Given: A Horizontal stack that is not stretchable (`stretchToFill = false`) and
    // contains two non-stretchable children (smallImage, largeImage).
    // When: `autoLayout` is called on the stack with targetWidth and targetHeight that are
    // larger than the stack's and children's initial maximum dimensions.
    // Then: The stack's horizontal and vertical min/max dimensions should be capped at their
    // original calculated maximum values (because `increaseMin` caps at max,
    // and `stretchToFill` is false).
    // And: The children's horizontal and vertical min/max dimensions should also be capped
    // at their original maximum values.
    @Test
    fun autoLayout_HorizontalStack_NotStretchable_WithNonStretchableChildren() {
        // Horizontal stack, not stretchToFill by default
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        // Save original max dimensions for assertion, as increaseMin will cap at max
        val originalStackHorizMax = horizontalStack.horizontally.max
        val originalStackVertMax = horizontalStack.vertically.max
        val originalItem1HorizMax = smallImage.horizontally.max
        val originalItem1VertMax = smallImage.vertically.max
        val originalItem2HorizMax = largeImage.horizontally.max
        val originalItem2VertMax = largeImage.vertically.max

        val targetWidth = 300f  // Larger than initial max
        val targetHeight = 200f // Larger than initial max

        horizontalStack.autoLayout(targetWidth, targetHeight)

        // Since stack is not stretchToFill, its dimensions should be capped by its original max
        // after the increaseMin calls. The stretch calls won't expand it further.
        assertEquals(
            "Stack HMax after autoLayout",
            originalStackHorizMax,
            horizontalStack.horizontally.max
        )
        assertEquals(
            "Stack VMax after autoLayout",
            originalStackVertMax,
            horizontalStack.vertically.max
        )
        // Min might have increased up to max
        assertEquals(
            "Stack HMin after autoLayout",
            originalStackHorizMax,
            horizontalStack.horizontally.min
        )
        assertEquals(
            "Stack VMin after autoLayout",
            originalStackVertMax,
            horizontalStack.vertically.min
        )


        // Children, also not stretchToFill, should also be capped at their original max.
        assertEquals(
            "smallImage HMax after autoLayout",
            originalItem1HorizMax,
            smallImage.horizontally.max
        )
        assertEquals(
            "smallImage VMax after autoLayout",
            originalItem1VertMax,
            smallImage.vertically.max
        )
        assertEquals(
            "smallImage HMin after autoLayout",
            originalItem1HorizMax,
            smallImage.horizontally.min
        )
        assertEquals(
            "smallImage VMin after autoLayout",
            originalItem1VertMax,
            smallImage.vertically.min
        )

        assertEquals(
            "largeImage HMax after autoLayout",
            originalItem2HorizMax,
            largeImage.horizontally.max
        )
        assertEquals(
            "largeImage VMax after autoLayout",
            originalItem2VertMax,
            largeImage.vertically.max
        )
        assertEquals(
            "largeImage HMin after autoLayout",
            originalItem2HorizMax,
            largeImage.horizontally.min
        )
        assertEquals(
            "largeImage VMin after autoLayout",
            originalItem2VertMax,
            largeImage.vertically.min
        )
    }

    // Given: A Vertical stack that is not stretchable (`stretchToFill = false`) and
    // contains two non-stretchable children (smallImage, largeImage).
    // When: `autoLayout` is called on the stack with targetWidth and targetHeight that are
    // larger than the stack's initial maximum dimensions.
    // Then: The stack's horizontal and vertical min/max dimensions should be capped at
    // their original calculated maximum values.
    @Test
    fun autoLayout_VerticalStack_NotStretchable_WithNonStretchableChildren() {

        // Vertical stack, not stretchToFill by default
        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(smallImage, largeImage)
        )

        val originalStackHorizMax = verticalStack.horizontally.max
        val originalStackVertMax = verticalStack.vertically.max

        val targetWidth = 300f
        val targetHeight = 400f

        verticalStack.autoLayout(targetWidth, targetHeight)

        assertEquals(
            "Stack HMax after autoLayout",
            originalStackHorizMax,
            verticalStack.horizontally.max
        )
        assertEquals(
            "Stack VMax after autoLayout",
            originalStackVertMax,
            verticalStack.vertically.max
        )
        assertEquals(
            "Stack HMin after autoLayout",
            originalStackHorizMax,
            verticalStack.horizontally.min
        )
        assertEquals(
            "Stack VMin after autoLayout",
            originalStackVertMax,
            verticalStack.vertically.min
        )
    }


    // Given: A Horizontal stack configured to be stretchable in its main (horizontal)
    // axis (`horizontally.stretchToFill = true`).
    // And: The stack contains one child (`stretchableChild`) that is also horizontally
    // stretchable, and one non-stretchable child (`fixedChild`).
    // When: `autoLayout` is called with a targetWidth (300f) and targetHeight (100f).
    // Then: The stack's horizontal min/max dimensions should stretch to targetWidth.
    // And: The stack's vertical min dimension should be its calculated height based on
    // children's max heights and padding (due to `increaseVerticalMin` and
    // non-stretchable vertical behavior).
    // And: The `fixedChild`'s horizontal min dimension should be its original max width
    // (due to `increaseHorizontalMin`).
    // And: The `stretchableChild`'s horizontal min dimension should take the remaining
    // width within the stack after accounting for stack spacing and the `fixedChild`'s width.
    @Test
    fun autoLayout_HorizontalStack_StretchableMainAxis_WithStretchableChild() {
        val stretchableChildWidth = smallImageWidth.apply { stretchToFill = true }
        val stretchableChild = Measurable(stretchableChildWidth, smallImageHeight)
            .apply { horizontally.stretchToFill = true }

        val fixedChild = Measurable(
            largeImageWidth, // Not stretchable width
            largeImageHeight
        )

        // Horizontal stack, set to stretch horizontally
        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(stretchableChild, fixedChild)
        ).apply { horizontally.stretchToFill = true }
        // Not vertically stretchable by default for this test case

        val targetWidth = 300f
        val targetHeight = 100f // Height for increaseVerticalMin and potential cross-axis stretch

        // Calculate expected height of stack if it's not vertically stretched
        // It will be based on max height of children + padding after increaseVerticalMin
        val expectedStackHeightAfterIncrease =
            padding + kotlin.math.max(smallImageHeightMax, largeImageHeightMax) + padding


        horizontalStack.autoLayout(targetWidth, targetHeight)

        // Stack should stretch to targetWidth
        assertEquals(
            "Stack HMin after autoLayout",
            targetWidth,
            horizontalStack.horizontally.min
        )
        assertEquals(
            "Stack HMax after autoLayout",
            targetWidth,
            horizontalStack.horizontally.max
        )

        // Stack's vertical dimension will be affected by increaseVerticalMin(targetHeight)
        // and then stretchVertically(targetHeight). If stack.vertically.stretchToFill
        // is false (default), it will be capped by its content's max height or
        // targetHeight if smaller.
        // Given the current Stack.Horizontal.stretchVertically, if
        // stack.vertically.stretchToFill is false, it does nothing to the stack's
        // own vertical dimensions.
        // So, the height will be determined by increaseVerticalMin.
        val finalStackHeight = kotlin.math.min(expectedStackHeightAfterIncrease, targetHeight)
        // However, increaseVerticalMin in HorizontalStack updates items first, then recalculates.
        // The stack's own vertically.min/max will be based on children's max height + padding.
        assertEquals(
            "Stack VMin after autoLayout",
            expectedStackHeightAfterIncrease,
            horizontalStack.vertically.min
        )


        // Fixed child's width should be its max (due to increaseHorizontalMin)
        assertEquals(
            "FixedChild HMin",
            largeImageWidthMax,
            fixedChild.horizontally.min
        )

        // Stretchable child should take remaining width
        val spaceForChildrenContent = targetWidth - horizontalStack.horizontalSpacing.min // p+g+p
        // or fixedChild.horizontally.max
        val expectedStretchableChildWidth = spaceForChildrenContent - fixedChild.horizontally.min
        assertEquals(
            "StretchableChild HMin",
            expectedStretchableChildWidth,
            stretchableChild.horizontally.min
        )
    }

    // Given: A Vertical stack configured to be stretchable in its main (vertical) axis
    // (`vertically.stretchToFill = true`).
    // And: The stack contains one child (`stretchableChild`) that is also vertically
    // stretchable, and one non-stretchable child (`fixedChild`).
    // When: `autoLayout` is called with a targetWidth (100f) and targetHeight (400f).
    // Then: The stack's vertical min/max dimensions should stretch to targetHeight.
    // And: The stack's horizontal min dimension should be its calculated width based on
    // children's max widths and padding.
    // And: The `fixedChild`'s vertical min dimension should be its original max height.
    // And: The `stretchableChild`'s vertical min dimension should take the remaining
    // height within the stack after accounting for stack spacing and the `fixedChild`'s height.
    @Test
    fun autoLayout_VerticalStack_StretchableMainAxis_WithStretchableChild() {
        val stretchableChildHeight = smallImageHeight.apply { stretchToFill = true }
        val stretchableChild = Measurable(smallImageWidth, stretchableChildHeight)
            .apply { vertically.stretchToFill = true }

        val fixedChild = Measurable(
            largeImageWidth,
            largeImageHeight // Not stretchable height
        )

        val verticalStack = Stack.Vertical(
            gutter,
            fixedSquarePadding,
            listOf(stretchableChild, fixedChild)
        ).apply { vertically.stretchToFill = true } // Stretch in main axis
        // Not horizontally stretchable by default

        val targetWidth = 100f
        val targetHeight = 400f

        // Expected width of stack if not horizontally stretched
        val expectedStackWidthAfterIncrease =
            padding + kotlin.math.max(smallImageWidthMax, largeImageWidthMax) + padding

        verticalStack.autoLayout(targetWidth, targetHeight)

        assertEquals(
            "Stack VMin after autoLayout",
            targetHeight,
            verticalStack.vertically.min
        )
        assertEquals(
            "Stack VMax after autoLayout",
            targetHeight,
            verticalStack.vertically.max
        )

        // Stack's horizontal dimension
        assertEquals(
            "Stack HMin after autoLayout",
            expectedStackWidthAfterIncrease,
            verticalStack.horizontally.min
        )

        // Fixed child's height should be its max
        assertEquals(
            "FixedChild VMin",
            largeImageHeightMax,
            fixedChild.vertically.min
        )

        // Stretchable child should take remaining height
        val spaceForChildrenContent = targetHeight - verticalStack.verticalSpacing.min // p+g+p
        val expectedStretchableChildHeight = spaceForChildrenContent - fixedChild.vertically.min
        assertEquals(
            "StretchableChild VMin",
            expectedStretchableChildHeight,
            stretchableChild.vertically.min
        )
    }

    // Given: A Horizontal stack configured to be stretchable in its cross (vertical)
    // axis (`vertically.stretchToFill = true`).
    // And: The stack contains one child (`child1`) that is also vertically stretchable,
    // and one non-stretchable child (`child2`).
    // When: `autoLayout` is called with a targetWidth (250f) and targetHeight (150f).
    // Then: The stack's vertical min/max dimensions should stretch to targetHeight.
    // And: The stack's horizontal min dimension should be its calculated width based on
    // children's max widths, gutter, and padding.
    // And: `child1`'s vertical min dimension should take the available height within the
    // stack after accounting for stack's vertical padding.
    // And: `child2`'s vertical min dimension should be its original max height (as it's
    // not stretchable and `increaseVerticalMin` would have capped it).
    @Test
    fun autoLayout_HorizontalStack_StretchableCrossAxis_WithStretchableChild() {
        val stretchableChildHeight = smallImageHeight.apply { stretchToFill = true }
        val child1 = Measurable(smallImageWidth, stretchableChildHeight)
            .apply { vertically.stretchToFill = true } // Stretchable in cross-axis

        val child2 = Measurable(
            largeImageWidth,
            largeImageHeight // Not stretchable height
        )

        val horizontalStack = Stack.Horizontal(
            gutter,
            fixedSquarePadding,
            listOf(child1, child2)
        ).apply { vertically.stretchToFill = true } // Stretch in cross-axis (vertical)
        // Not horizontally stretchable by default

        val targetWidth = 250f
        val targetHeight = 150f

        // Expected width of stack if not horizontally stretched
        val expectedStackWidthAfterIncrease =
            padding + smallImageWidthMax + gutter + largeImageWidthMax + padding


        horizontalStack.autoLayout(targetWidth, targetHeight)

        // Stack should stretch vertically to targetHeight
        assertEquals(
            "Stack VMin after autoLayout",
            targetHeight,
            horizontalStack.vertically.min
        )
        assertEquals(
            "Stack VMax after autoLayout",
            targetHeight,
            horizontalStack.vertically.max
        )

        // Stack's horizontal dimension
        assertEquals(
            "Stack HMin after autoLayout",
            expectedStackWidthAfterIncrease,
            horizontalStack.horizontally.min
        )


        // Child1 (stretchable vertically) should take up available height within padding
        val heightForChildrenContent = targetHeight - horizontalStack.verticalSpacing.min // p+p
        assertEquals(
            "Child1 VMin",
            heightForChildrenContent,
            child1.vertically.min
        )

        // Child2 (not stretchable vertically) should be its max height
        // (due to increaseVerticalMin) or targetHeight if smaller, but
        // increaseVerticalMin logic in HorizontalStack is complex.
        // After stretchVertically, child2.stretchVertically(heightForChildrenContent) is called.
        // If child2 is not stretchToFill, its Resizable.stretchTo will cap it.
        assertEquals(
            "Child2 VMin",
            largeImageHeightMax,
            child2.vertically.min
        )
    }
}
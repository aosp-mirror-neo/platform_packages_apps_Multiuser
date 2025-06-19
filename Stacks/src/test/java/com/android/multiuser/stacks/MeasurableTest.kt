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
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import kotlin.test.assertTrue
import com.android.multiuser.stacks.*

@RunWith(AndroidJUnit4::class)
class MeasurableTest {

    /**
     * Given two Measurable objects with defined Resizable dimensions
     * When the add method is called on one Measurable with the other as an argument
     * Then the resulting Measurable should have dimensions that are the sum of the corresponding
     * dimensions of the two input Measurables
     */
    @Test
    fun AddMeasurablesTest() {
        val r11 = Resizable(11f, 12f)
        val r12 = Resizable(23f, 24f)
        val m1 = Measurable(r11, r12)

        val r21 = Resizable(9f, 15f)
        val r22 = Resizable(13f, 17f)
        val m2 = Measurable(r21, r22)

        val m3 = m1.add(m2)
        assertEquals(20f, m3.horizontally.min)
        assertEquals(27f, m3.horizontally.max)
        assertEquals(36f, m3.vertically.min)
        assertEquals(41f, m3.vertically.max)

        val m4 = m2.add(m1)
        assertEquals(20f, m4.horizontally.min)
        assertEquals(27f, m4.horizontally.max)
        assertEquals(36f, m4.vertically.min)
        assertEquals(41f, m4.vertically.max)
    }

    /**
     * Given a Measurable object with defined Resizable dimensions and a float value
     * When the add method is called on the Measurable with the float value as an argument
     * Then the resulting Measurable should have its min and max values for both horizontal and
     * vertical dimensions increased by the float value
     */
    @Test
    fun AddFloat() {
        val r11 = Resizable(11f, 12f)
        val r12 = Resizable(23f, 24f)
        val m1 = Measurable(r11, r12)

        val m2 = m1.add(2f)
        assertEquals(13f, m2.horizontally.min)
        assertEquals(14f, m2.horizontally.max)
        assertEquals(25f, m2.vertically.min)
        assertEquals(26f, m2.vertically.max)
    }

    /**
     * Given two Measurable objects with defined Resizable dimensions
     * When the max method is called on one Measurable with the other as an argument
     * Then the resulting Measurable should have dimensions where the min is the maximum of the two input mins, and the max is the maximum of the two input maxs, for both horizontal and vertical components
     */
    @Test
    fun MaxMeasurablesTest() {
        val r11 = Resizable(11f, 12f)
        val r12 = Resizable(23f, 24f)
        val m1 = Measurable(r11, r12)

        val r21 = Resizable(9f, 15f)
        val r22 = Resizable(13f, 17f)
        val m2 = Measurable(r21, r22)

        val m = m1.max(m2)
        assertEquals(11f, m.horizontally.min)
        assertEquals(15f, m.horizontally.max)
        assertEquals(23f, m.vertically.min)
        assertEquals(24f, m.vertically.max)
    }

    /**
     * Given a Measurable object with vertical Resizable dimensions
     * When increaseVerticalMin is called with a totalAvailable value greater than the vertical max
     * Then the vertical min and max of the Measurable should be updated to its original max value,
     * and the method should return this new min value
     */
    @Test
    fun IncreaseVerticalWhenIncreaseIsBiggerThanVerticalTest() {
        val horizontal = Resizable(12f, 24f)
        val vertical = Resizable(10f, 20f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.increaseVerticalMin(100f)

        assertEquals(20f, r)
        assertEquals(20f, m1.vertically.min)
        assertEquals(20f, m1.vertically.max)
        assertEquals(12f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
    }

    /**
     * Given a Measurable object with vertical Resizable dimensions
     * When increaseVerticalMin is called with a totalAvailable value smaller than the vertical min
     * Then the vertical dimensions of the Measurable should remain unchanged, and the method should
     * return the original min value
     */
    @Test
    fun NotIncreaseVerticalWhenIncreaseIsSmallerThanVerticalTest() {
        val horizontal = Resizable(12f, 24f)
        val vertical = Resizable(10f, 20f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.increaseVerticalMin(5f)

        assertEquals(10f, r)
        assertEquals(10f, m1.vertically.min)
        assertEquals(20f, m1.vertically.max)
        assertEquals(12f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
    }

    /**
     * Given a Measurable object whose vertical Resizable dimension has stretchToFill set to true
     * When stretchVertically is called with a totalAvailable value
     * Then the vertical min and max of the Measurable should be updated to the totalAvailable value,
     * and the method should return this new min value
     */
    @Test
    fun StretchVerticalTestWhenVerticalIsStretchable() {
        val horizontal = Resizable(23f, 24f)
        val vertical = Resizable(11f, 12f).apply { stretchToFill = true }
        val m1 = Measurable(horizontal, vertical)
        var r = m1.stretchVertically(100f)

        assertEquals(100f, r)
        assertEquals(100f, m1.vertically.min)
        assertEquals(100f, m1.vertically.max)
        assertEquals(23f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
    }

    /**
     * Given a Measurable object whose vertical Resizable dimension has stretchToFill set to false
     * When stretchVertically is called with a totalAvailable value
     * Then the vertical dimensions of the Measurable should remain unchanged, and the method should
     * return the original min value
     */
    @Test
    fun NotStretchVerticalWhenVerticalIsNotStretchableTest() {
        val horizontal = Resizable(23f, 24f).apply { stretchToFill = true }
        val vertical = Resizable(11f, 12f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.stretchVertically(100f)

        assertEquals(11f, r)
        assertEquals(11f, m1.vertically.min)
        assertEquals(12f, m1.vertically.max)
        assertEquals(23f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
    }

    /**
     * Given a Measurable object with horizontal Resizable dimensions
     * When increaseHorizontalMin is called with a totalAvailable value greater than horizontal max
     * Then the horizontal min and max of the Measurable should be updated to its original max value,
     * and the method should return this new min value
     */
    @Test
    fun IncreaseHorizontalWhenIncreaseIsBiggerThanHorizontalTest() {
        val horizontal = Resizable(12f, 24f)
        val vertical = Resizable(10f, 20f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.increaseHorizontalMin(100f)

        assertEquals(24f, r)
        assertEquals(24f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
        assertEquals(10f, m1.vertically.min)
        assertEquals(20f, m1.vertically.max)
    }

    /**
     * Given a Measurable object with horizontal Resizable dimensions
     * When increaseHorizontalMin is called with a totalAvailable value smaller than the horizontal min
     * Then the horizontal dimensions of the Measurable should remain unchanged, and the method should return the original min value
     */
    @Test
    fun NotIncreaseHorizontalWhenIncreaseIsSmallerThanHorizontalTest() {
        val horizontal = Resizable(12f, 24f)
        val vertical = Resizable(10f, 20f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.increaseHorizontalMin(5f)

        assertEquals(12f, r)
        assertEquals(12f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
        assertEquals(10f, m1.vertically.min)
        assertEquals(20f, m1.vertically.max)
    }

    /**
     * Given a Measurable object whose horizontal Resizable dimension has stretchToFill set to true
     * When stretchHorizontally is called with a totalAvailable value
     * Then the horizontal min and max of the Measurable should be updated to the totalAvailable
     * value, and the method should return this new min value
     */
    @Test
    fun StretchHorizontalTestWhenHorizontalIsStretchable() {
        val horizontal = Resizable(12f, 24f).apply { stretchToFill = true }
        val vertical = Resizable(11f, 12f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.stretchHorizontally(100f)

        assertEquals(100f, r)
        assertEquals(100f, m1.horizontally.min)
        assertEquals(100f, m1.horizontally.max)
        assertEquals(11f, m1.vertically.min)
        assertEquals(12f, m1.vertically.max)
    }

    /**
     * Given a Measurable object whose horizontal Resizable dimension has stretchToFill set to false
     * When stretchHorizontally is called with a totalAvailable value
     * Then the horizontal dimensions of the Measurable should remain unchanged
     * (method returns original min)
     */
    @Test
    fun NotStretchHorizontalWhenHorizontalIsNotStretchableTest() {
        val horizontal = Resizable(12f, 24f)
        val vertical = Resizable(11f, 12f)
        val m1 = Measurable(horizontal, vertical)
        var r = m1.stretchHorizontally(100f) // The return value of stretchHorizontally is the new min

        assertEquals(12f, r) // Asserting the return value is the original min
        assertEquals(12f, m1.horizontally.min)
        assertEquals(24f, m1.horizontally.max)
        assertEquals(11f, m1.vertically.min)
        assertEquals(12f, m1.vertically.max)
    }
}
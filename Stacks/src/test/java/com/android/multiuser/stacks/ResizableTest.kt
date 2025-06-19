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
class ResizableTest {

    // Given two Resizable objects with defined min and max values
    // When the add method is called on one Resizable with the other as an argument
    // Then the resulting Resizable should have its min and max values as the sum of the
    // corresponding input Resizables' min and max values
    @Test
    fun testAddingResizables() {
        val r1 = Resizable(11f, 12f)
        val r2 = Resizable(23f, 24f)
        var r3 = r1.add(r2)
        assertEquals(34f, r3.min)
        assertEquals(36f, r3.max)
        r3 = r2.add(r1)
        assertEquals(34f, r3.min)
        assertEquals(36f, r3.max)
    }

    // Given a Resizable object with defined min and max values and a float value
    // When the add method is called on the Resizable with the float value as an argument
    // Then the resulting Resizable should have its min and max values increased by the float value
    @Test
    fun testAddingFloat() {
        val r1 = Resizable(11f, 12f)
        var r2 = r1.add(2f)
        assertEquals(13f, r2.min)
        assertEquals(14f, r2.max)
    }

    // Given two Resizable objects with defined min and max values
    // When the max method is called on one Resizable with the other as an argument
    // Then the resulting Resizable should have its min value as the maximum of the two input
    // Resizables' min values, and its max value as the maximum of the two input Resizables' max
    @Test
    fun testMax() {
        val r1 = Resizable(11f, 12f)
        val r2 = Resizable(23f, 24f)
        var r3 = r1.max(r2)
        assertEquals(23f, r3.min)
        assertEquals(24f, r3.max)
        r3 = r2.max(r1)
        assertEquals(23f, r3.min)
        assertEquals(24f, r3.max)
    }

    // Given a Resizable object with defined min and max values and a float scalar
    // When the times method is called on the Resizable with the float scalar as an argument
    // Then the resulting Resizable should have its min and max multiplied by the float scalar
    @Test
    fun testTimes() {
        val r1 = Resizable(11f, 12f)
        var r2 = r1.times(2f)
        assertEquals(22f, r2.min)
        assertEquals(24f, r2.max)
    }

    // Given a Resizable object with initial min and max values
    // When increaseMin is called with a value greater than the current min but less than or equal
    // to the current max
    // Then the Resizable's min should be updated to the new value, max should remain unchanged,
    // and the method should return the new min
    // When increaseMin is called with a value less than the current min
    // Then the Resizable's min and max should remain unchanged, and the method should return the
    // current min
    // When increaseMin is called with a value greater than the current max
    // Then the Resizable's min should be updated to its original max value, max should also be
    // updated to this value (effectively clamping at max), and the method should return this new min (original max)
    @Test
    fun testIncreaseMin() {
        val r1 = Resizable(11f, 18f)

        var r2 = r1.increaseMin(15f)
        assertEquals(15f, r2)
        assertEquals(15f, r1.min)
        assertEquals(18f, r1.max)

        r2 = r1.increaseMin(5f)
        assertEquals(15f, r2)
        assertEquals(15f, r1.min)
        assertEquals(18f, r1.max)

        r2 = r1.increaseMin(20f)
        assertEquals(18f, r2)
        assertEquals(18f, r1.min)
        assertEquals(18f, r1.max)
    }

    // Given a Resizable object with stretchToFill set to true
    // When stretchTo is called with a specific totalAvailable value
    // Then the Resizable's min and max should both be updated to the totalAvailable value,
    // and the method should return this value
    @Test
    fun testStretchTo() {
        val r1 = Resizable(11f, 18f)
        r1.stretchToFill = true
        var r2 = r1.stretchTo(15f)
        assertEquals(15f, r2)
        assertEquals(15f, r1.min)
        assertEquals(15f, r1.max)

        r2 = r1.stretchTo(20f)
        assertEquals(20f, r2)
        assertEquals(20f, r1.min)
        assertEquals(20f, r1.max)

        r2 = r1.stretchTo(5f)
        assertEquals(5f, r2)
        assertEquals(5f, r1.min)
        assertEquals(5f, r1.max)
    }

    // Given a Resizable object with stretchToFill set to false
    // When stretchTo is called with any totalAvailable value
    // Then the Resizable's min and max should remain unchanged, and the method should return
    // the original min value
    @Test
    fun testNotStretchTo() {
        val r1 = Resizable(11f, 18f)
        r1.stretchToFill = false
        var r2 = r1.stretchTo(15f)
        assertEquals(11f, r2)
        assertEquals(11f, r1.min)
        assertEquals(18f, r1.max)

        r2 = r1.stretchTo(20f)
        assertEquals(11f, r2)
        assertEquals(11f, r1.min)
        assertEquals(18f, r1.max)

        r2 = r1.stretchTo(5f)
        assertEquals(11f, r2)
        assertEquals(11f, r1.min)
        assertEquals(18f, r1.max)
    }
}
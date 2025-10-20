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

package com.android.multiuser.widget.viewmodel.stacks

import com.android.multiuser.stacks.Stack
import com.android.multiuser.widget.viewmodel.measurables.SpacerMeasurable


open class VerticalPaddingWrapperStack<T: Stack>(stack: T, protected val minSize : Float)
    : Stack.Vertical(list =  listOf(SpacerMeasurable(), stack, SpacerMeasurable())) {

    fun getStack() = list[1] as T

    override fun recalculateVertical() {
        if (vertically.max < minSize) {
            val singlePadding = (minSize - vertically.min)/2
            list[0].vertically.min = singlePadding
            list[0].vertically.max = singlePadding
            list[2].vertically.min = singlePadding
            list[2].vertically.max = singlePadding

            list[0].horizontally.min = list[1].horizontally.min
            list[0].horizontally.max = list[1].horizontally.max
            list[2].horizontally.min = list[1].horizontally.min
            list[2].horizontally.max = list[1].horizontally.max
        }
        // Recalculate super again once spacers where setup
        super.recalculateVertical()
    }
}

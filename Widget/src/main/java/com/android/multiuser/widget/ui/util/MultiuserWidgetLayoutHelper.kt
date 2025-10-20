/*
 * Copyright (C) 2024 The Android Open Source Project
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

package com.android.multiuser.widget.ui.util

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.unit.ColorProvider
import com.android.multiuser.widget.R

@Composable
fun ResourceString (resId: Int): String {
    return LocalContext.current.getString(resId)
}

@Composable
fun ResourceDp (resId: Int): Dp {
    return Dp(LocalContext.current.resources.getDimension(resId))
}

@Composable
fun GridItemModifierForIndex(index: Int, gridCells: Int, numRows: Int): GlanceModifier {
    val cellSpacing = ResourceDp(R.dimen.widget_cell_spacing)

    // Cell spacing is achieved by allocating equal amount of padding to each cell. Cells on edge
    // apply it completely to inner sides, while cells not on edge apply it evenly on sides.
    val perCellHorizontalPadding = (cellSpacing * (gridCells - 1)) / gridCells
    val perCellVerticalPadding = (cellSpacing * (numRows - 1)) / numRows

    val row = index / gridCells
    val column = index % gridCells

    val cellTopPadding = when (row) {
        0 -> 0.dp
        numRows - 1 -> perCellVerticalPadding
        else -> perCellVerticalPadding / 2
    }

    val cellBottomPadding = when (row) {
        0 -> perCellVerticalPadding
        numRows - 1 -> 0.dp
        else -> perCellVerticalPadding / 2
    }

    val cellStartPadding = when (column) {
        0 -> 0.dp
        gridCells - 1 -> perCellHorizontalPadding
        else -> perCellHorizontalPadding / 2
    }

    val cellEndPadding = when (column) {
        0 -> perCellHorizontalPadding
        gridCells - 1 -> 0.dp
        else -> perCellHorizontalPadding / 2
    }

    return GlanceModifier
        .fillMaxSize()
        .padding(
            start = cellStartPadding,
            end = cellEndPadding,
            top = cellTopPadding,
            bottom = cellBottomPadding
        )
}

@Composable
fun NumberOfColumns(widgetWidth: DpSize): Int {
    return when (WidgetLayoutSize.fromLocalSize(widgetWidth)) {
        WidgetLayoutSize.Small -> 1
        WidgetLayoutSize.Medium -> 2
        WidgetLayoutSize.Large -> 3
        else -> {4}
    }
}

// Size of the widget per the reference breakpoints. Each size has a different number of
// users per row.
private enum class WidgetLayoutSize(val maxWidth: Dp) {
    // One user per row
    Small (maxWidth = 300.dp),

    // Two users per row
    Medium(maxWidth = 400.dp),

    // Three users per row
    Large(maxWidth = 500.dp),

    // Four users per row
    ExtraLarge(maxWidth = Dp.Infinity);

    companion object {
        // Returns the corresponding WidgetLayoutSize to be considered for the current
        // widget size.
        fun fromLocalSize(size: DpSize): WidgetLayoutSize {
            val widgetLayoutSizeOptions = values()

            widgetLayoutSizeOptions.forEach {
                if (size.width < it.maxWidth) {
                    return it
                }
            }

            // If the size isn't mapped correctly, default to Small.
            return Small
        }
    }
}

fun ColorProvider.byState(context: Context, enabled: Boolean): ColorProvider {
    if (enabled)
        return this
    return ColorProvider(this.getColor(context).copy(alpha = 0.38f))
}


const val EMPTY_STRING = ""

@Composable
fun rememberCurrentResources(): MutableState<Resources> {
    val context by rememberUpdatedState(LocalContext.current)
    val resources = remember { mutableStateOf(context.resources, neverEqualPolicy()) }
    DisposableEffect(context) {
        val callback = object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration) {
                // retrieve the current resources
                resources.value = context.resources
            }
            override fun onLowMemory() {}
        }
        context.registerComponentCallbacks(callback)
        onDispose {
            context.unregisterComponentCallbacks(callback)
        }
    }
    return resources
}

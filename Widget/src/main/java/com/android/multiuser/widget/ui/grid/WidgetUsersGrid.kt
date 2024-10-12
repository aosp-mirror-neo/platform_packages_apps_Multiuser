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

package com.android.multiuser.widget.ui.grid

import androidx.compose.runtime.Composable
import androidx.glance.LocalSize
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import com.android.multiuser.widget.model.MultiuserWidgetModel
import com.android.multiuser.widget.ui.grid.item.WidgetGridItem
import com.android.multiuser.widget.ui.util.GridItemModifierForIndex
import com.android.multiuser.widget.ui.util.NumberOfColumns
import com.android.multiuser.widget.ui.util.WidgetGlanceModifiers.VerticalGridBoxModifier
import kotlin.math.ceil

@Composable
fun WidgetUsersGrid(model: MultiuserWidgetModel) {
    Box(
        modifier = VerticalGridBoxModifier()
    ) {
        val items = model.items
        val gridCells = NumberOfColumns(LocalSize.current)
        val numRows = ceil(items.size.toDouble() / gridCells).toInt()

        LazyVerticalGrid(
            gridCells = GridCells.Fixed(gridCells),
            horizontalAlignment = Alignment.Start
        ) {
            itemsIndexed(items) { index, item ->
                WidgetGridItem(
                    item = item,
                    modifier = GridItemModifierForIndex(index, gridCells, numRows)
                )
            }
        }
    }
}

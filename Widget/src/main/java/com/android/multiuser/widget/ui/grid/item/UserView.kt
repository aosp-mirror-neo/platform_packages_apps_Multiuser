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

package com.android.multiuser.widget.ui.grid.item

import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.Text
import com.android.multiuser.widget.R
import com.android.multiuser.widget.ui.util.ResourceDp
import com.android.multiuser.widget.ui.util.WidgetGlanceModifiers.UserBoxModifier
import com.android.multiuser.widget.ui.util.WidgetGlanceModifiers.TrailingBottomModifier
import com.android.multiuser.widget.ui.util.WidgetTextStyles
import com.android.multiuser.widget.viewmodel.UserViewModel

/**
 * Displays user data in the widget.
 */
@Composable
fun UserView (
    viewModel: UserViewModel,
    modifier: GlanceModifier,
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top
        Box (modifier = UserBoxModifier(viewModel.isSelected)
            .padding(bottom = ResourceDp(R.dimen.widget_grid_top_spacing))) {
            Image(
                provider = ImageProvider(Icon.createWithContentUri(viewModel.iconUri))
                    .takeIf{ viewModel.iconUri != null }
                    ?: ImageProvider(viewModel.resourceId),
                contentScale = ContentScale.Fit,
                contentDescription = viewModel.contentDescription,
                modifier = GlanceModifier.fillMaxWidth().wrapContentHeight()
            )
        }

        // Bottom
        Row {
            Column(
                modifier = GlanceModifier.defaultWeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = viewModel.name,
                    maxLines = 1,
                    style = WidgetTextStyles.ImageTitle(),
                    modifier =  GlanceModifier.padding(start = R.dimen.widget_text_start_margin)
                )
            }

            // Trailing bottom
            Spacer(modifier = TrailingBottomModifier())
        }
    }
}

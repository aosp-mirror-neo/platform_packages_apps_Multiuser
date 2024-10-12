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

package com.android.multiuser.widget.ui.view.layout

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import androidx.glance.text.Text
import com.android.multiuser.widget.ui.util.ResourceString
import com.android.multiuser.widget.ui.util.WidgetTextStyles
import com.android.multiuser.R

@Composable
fun ErrorWidget() {
    Box(modifier = GlanceModifier.fillMaxSize()) {
        Text(
            text = ResourceString(R.string.multiuser_widget_error_message),
            modifier = GlanceModifier.size(R.dimen.error_widget_font_size),
            style = WidgetTextStyles.ErrorMessage()
        )
    }
}

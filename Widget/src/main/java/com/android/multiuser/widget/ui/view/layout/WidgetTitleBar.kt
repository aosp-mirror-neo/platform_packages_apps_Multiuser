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

import android.content.ComponentName
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import com.android.multiuser.widget.R
import com.android.multiuser.widget.ui.util.EMPTY_STRING
import com.android.multiuser.widget.ui.util.ResourceDp
import com.android.multiuser.widget.ui.util.ResourceString

@Composable
fun WidgetTitleBar() {
    val title = ResourceString(R.string.multiuser_widget_title)
        .takeIf{ LocalSize.current.width >= ResourceDp(R.dimen.widget_text_breakpoint) }
        ?: EMPTY_STRING

    TitleBar(
        startIcon = ImageProvider(R.drawable.account_circle),
        title = title,
        iconColor = GlanceTheme.colors.primary,
        textColor = GlanceTheme.colors.onSurface,
        actions = {
            CircleIconButton(
                imageProvider = ImageProvider(R.drawable.settings),
                contentDescription = ResourceString(R.string.widget_settings_button_content_description),
                contentColor = GlanceTheme.colors.secondary,
                backgroundColor = null,
                onClick = actionStartActivity(Intent(Settings.ACTION_USER_SETTINGS)
                        .setComponent(
                                ComponentName("com.android.settings",
                                        "com.android.settings.Settings\$UserSettingsActivity")
                                )
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)),
            )
        }
    )
}

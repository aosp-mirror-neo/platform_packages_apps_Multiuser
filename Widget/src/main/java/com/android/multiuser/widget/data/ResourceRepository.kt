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

package com.android.multiuser.widget.data

import android.content.ComponentName
import android.content.Intent
import android.content.res.Resources
import android.provider.Settings
import com.android.multiuser.widget.R

class ResourceRepository(val resources: Resources) {
    class DialogViewModel(
        val message: String,
        val positiveButtonText: String? = null,
        val negativeButtonText: String,
        val actionIntent: Intent? = null
    )

    fun getSwitchFailed() = DialogViewModel(
        message = resources.getString(R.string.widget_switch_failed_dialog_message),
        negativeButtonText =
            resources.getString(R.string.dialog_close_button_text),
    )

    fun getSwitchDisabled() = DialogViewModel(
        message = resources.getString(R.string.widget_switch_disabled_dialog_message),
        positiveButtonText =
            resources.getString(R.string.widget_switch_disabled_dialog_open_settings_button),
        negativeButtonText =
            resources.getString(R.string.dialog_close_button_text),
        actionIntent = Intent(Settings.ACTION_USER_SETTINGS)
            .setComponent(
                ComponentName("com.android.settings",
                    "com.android.settings.Settings\$UserSettingsActivity")
            )
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK),
    )
}

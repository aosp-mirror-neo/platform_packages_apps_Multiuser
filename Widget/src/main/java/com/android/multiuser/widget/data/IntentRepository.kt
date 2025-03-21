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
import android.os.UserManager
import com.android.multiuser.widget.R
import com.android.multiuser.widget.data.model.UserSwitchRestrictions

class IntentRepository(val res: Resources) {
    fun getUserSwitchRestrictedIntent(restriction: UserSwitchRestrictions) =
        Intent(Settings.ACTION_USER_SETTINGS)
            .setComponent(
                ComponentName(
                    "com.android.multiuser",
                    "com.android.multiuser.widget.ui.DialogActivity"
                )
            )
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra("title", res.getUserSwitchRestrictedTitle(restriction))
            .putExtra("message", res.getUserSwitchRestrictedMessage(restriction))
            .multiuserSettingsIntent(restriction)

    private fun Resources.getUserSwitchRestrictedTitle(
        restriction: UserSwitchRestrictions
    ): String {
        return when (restriction) {
            UserSwitchRestrictions.WORK_POLICY ->
                getString(R.string.widget_switch_work_restriction_dialog_title)
            UserSwitchRestrictions.ONCALL_OR_LOCKED ->
                getString(R.string.widget_switch_not_allowed_dialog_title)
            UserSwitchRestrictions.DISABLED ->
                getString(R.string.widget_switch_disabled_dialog_title)
            else ->
                getString(R.string.widget_switch_failed_dialog_title)
        }
    }

    private fun Resources.getUserSwitchRestrictedMessage(
        restriction: UserSwitchRestrictions
    ): String {
        return when (restriction) {
            UserSwitchRestrictions.WORK_POLICY ->
                getString(R.string.widget_switch_work_restriction_dialog_message)
            // impossible to show widged on locked system?
            UserSwitchRestrictions.ONCALL_OR_LOCKED ->
                getString(R.string.widget_switch_not_allowed_dialog_message)
            UserSwitchRestrictions.DISABLED ->
                getString(R.string.widget_switch_disabled_dialog_message)
            else ->
                getString(R.string.widget_switch_failed_dialog_message)
        }
    }

    private fun Intent.multiuserSettingsIntent(
        restriction: UserSwitchRestrictions
    ): Intent {
        if (restriction == UserSwitchRestrictions.DISABLED) {
            putExtra("intent_action", Settings.ACTION_USER_SETTINGS)
            putExtra("intent_package", "com.android.settings")
            putExtra(
                "intent_class",
                "com.android.settings.Settings\$UserSettingsActivity"
            )
            putExtra(
                "intent_flags",
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            putExtra(
                "action_text",
                res.getString(
                    R.string.widget_switch_disabled_dialog_open_settings_button
                )
            )
        }
        return this
    }
}

fun Intent.getActionIntent() : Intent? {
    var intent: Intent? = null
    getStringExtra("intent_action")?.let { action ->
        intent = Intent(action)
        getStringExtra("intent_package")?.let { intentPackage ->
            getStringExtra("intent_class")?.let { intentClass ->
                intent.component = ComponentName(intentPackage, intentClass)
            }
        }
        getIntExtra("intent_flags", 0)?.let { intent.flags = it }
    }
    return intent
}



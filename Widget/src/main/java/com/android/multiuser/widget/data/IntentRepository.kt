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
import com.android.multiuser.widget.data.model.UserSwitchRestrictions
import com.android.multiuser.widget.data.model.UserAddRestriction

class IntentRepository(val res: Resources) {
    fun getAddUserRestrictedIntent(restriction: UserAddRestriction) =
        Intent(Settings.ACTION_USER_SETTINGS)
            .setComponent(
                ComponentName(
                    ERROR_DIALOGS_PACKAGE,
                    ERROR_DILOGS_ACTIVITY
                )
            )
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(TITLE, res.getAddUserRestrictedTitle(restriction))
            .putExtra(MESSAGE, res.getAddUserRestrictedMessage(restriction))
            .apply {
                if (restriction == UserAddRestriction.SWITCH_DISABLED
                    || restriction == UserAddRestriction.DISABLED_ON_LOCKSCREEN ) {
                    multiuserSettingsIntent()
                        .putExtra(
                            ACTION_TEXT,
                            res.getString(
                                R.string.widget_switch_disabled_dialog_open_settings_button
                            )
                        )
                }
            }

    fun getUserSwitchRestrictedIntent(restriction: UserSwitchRestrictions) =
        Intent(Settings.ACTION_USER_SETTINGS)
            .setComponent(
                ComponentName(
                    ERROR_DIALOGS_PACKAGE,
                    ERROR_DILOGS_ACTIVITY
                )
            )
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(TITLE, res.getUserSwitchRestrictedTitle(restriction))
            .putExtra(MESSAGE, res.getUserSwitchRestrictedMessage(restriction))
            .apply {
                if (restriction == UserSwitchRestrictions.DISABLED) {
                    multiuserSettingsIntent()
                        .putExtra(
                            ACTION_TEXT,
                            res.getString(
                                R.string.widget_switch_disabled_dialog_open_settings_button
                            )
                    )
                }
            }

    fun getSettingsIntent() = Intent(Settings.ACTION_USER_SETTINGS)
            .setComponent(
                ComponentName(
                    SETTINGS_PACKAGE,
                    SETTINGS_ACTIVITY
                )
            )
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

    fun getUserSwitcherIntent() = Intent(USER_SWITCHER_ACTION)
        .setComponent(
            ComponentName(
                USER_DIALOGS_PACKAGE,
                USER_SWITCHER_RECEIVER
            )
        )

    fun getAddUserIntent() = Intent()
        .setComponent(
            ComponentName(
                USER_DIALOGS_PACKAGE,
                ADD_USER_ACTIVITY
            )
        )
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

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
            UserSwitchRestrictions.ONCALL_OR_LOCKED ->
                getString(R.string.widget_switch_not_allowed_dialog_message)
            UserSwitchRestrictions.DISABLED ->
                getString(R.string.widget_switch_disabled_dialog_message)
            else ->
                getString(R.string.widget_switch_failed_dialog_message)
        }
    }


    private fun Resources.getAddUserRestrictedTitle(
        restriction: UserAddRestriction
    ): String {
        return when (restriction) {
            UserAddRestriction.WORK_POLICY ->
                // Work policy title is the same for adding and switching users
                getString(R.string.widget_switch_work_restriction_dialog_title)
            UserAddRestriction.ONCALL_OR_LOCKED ->
                getString(R.string.widget_add_not_allowed_dialog_title)
            UserAddRestriction.NOT_ADMIN_OR_SYSTEM ->
                getString(R.string.widget_add_not_admin_dialog_title)
            UserAddRestriction.SWITCH_DISABLED ->
                getString(R.string.widget_add_disabled_dialog_title)
            UserAddRestriction.DISABLED_ON_LOCKSCREEN ->
                getString(R.string.widget_add_from_hub_dialog_title)
            UserAddRestriction.MAX_USER_LIMIT ->
                getString(R.string.widget_add_max_limit_dialog_title)
            else ->
                getString(R.string.widget_add_failed_dialog_title)
        }
    }

    private fun Resources.getAddUserRestrictedMessage(
        restriction: UserAddRestriction
    ): String {
        return when (restriction) {
            UserAddRestriction.WORK_POLICY ->
                getString(R.string.widget_add_work_restriction_dialog_message)
            // Impossible to show widget on locked system?
            UserAddRestriction.ONCALL_OR_LOCKED ->
                getString(R.string.widget_add_not_allowed_dialog_message)
            UserAddRestriction.NOT_ADMIN_OR_SYSTEM ->
                getString(R.string.widget_add_not_admin_dialog_message)
            // Disabled by user switch as flow add and switch user instantly
            UserAddRestriction.SWITCH_DISABLED ->
                getString(R.string.widget_switch_disabled_dialog_message)
            UserAddRestriction.DISABLED_ON_LOCKSCREEN ->
                getString(R.string.widget_add_from_hub_dialog_message)
            UserAddRestriction.MAX_USER_LIMIT ->
                getString(R.string.widget_add_max_limit_dialog_message)
            else ->
                getString(R.string.widget_switch_failed_dialog_message)
        }
    }

    private fun Intent.multiuserSettingsIntent() : Intent {
        putExtra(ACTION, Settings.ACTION_USER_SETTINGS)
        putExtra(PACKAGE, SETTINGS_PACKAGE)
        putExtra(CLASS, SETTINGS_ACTIVITY)
        putExtra(
            FLAGS,
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        return this
    }

    companion object {
        const val TITLE = "title"
        const val MESSAGE = "message"
        const val ACTION = "intent_action"
        const val PACKAGE = "intent_package"
        const val CLASS = "intent_class"
        const val FLAGS = "intent_flags"
        const val ACTION_TEXT = "action_text"

        const val SETTINGS_PACKAGE = "com.android.settings"
        const val SETTINGS_ACTIVITY = "com.android.settings.Settings\$UserSettingsActivity"
        const val ERROR_DIALOGS_PACKAGE = "com.android.multiuser"
        const val ERROR_DILOGS_ACTIVITY = "com.android.multiuser.widget.ui.DialogActivity"

        const val USER_DIALOGS_PACKAGE = "com.android.systemui"
        const val ADD_USER_ACTIVITY = "com.android.systemui.user.CreateUserActivity"
        const val USER_SWITCHER_RECEIVER = "com.android.systemui.user.UserDialogReceiver"
        const val USER_SWITCHER_ACTION = "com.android.systemui.action.LAUNCH_USER_SWITCHER_DIALOG"
    }
}

fun Intent.getActionIntent() : Intent? {
    var intent: Intent? = null
    getStringExtra(IntentRepository.ACTION)?.let { action ->
        intent = Intent(action)
        getStringExtra(IntentRepository.PACKAGE)?.let { intentPackage ->
            getStringExtra(IntentRepository.CLASS)?.let { intentClass ->
                intent.component = ComponentName(intentPackage, intentClass)
            }
        }
        getIntExtra(IntentRepository.FLAGS, 0)?.let { intent.flags = it }
    }
    return intent
}

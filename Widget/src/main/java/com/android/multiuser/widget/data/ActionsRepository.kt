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

import android.content.res.Resources
import android.multiuser.Flags
import com.android.multiuser.widget.data.model.Action
import com.android.multiuser.widget.R

class ActionsRepository(private val resources: Resources) {
    fun getAddUserAction() = Action(
        title = resources.getString(R.string.add_user_button_title),
        contentDescription = resources.getString(R.string.add_user_button_content_description),
        resourceId = if(Flags.widgetScalingBugfix()) R.drawable.add_24 else R.drawable.person_add
    )

    fun getSwitchUserAction() = Action(
        title = resources.getString(R.string.switch_user_button_title),
        contentDescription = resources.getString(R.string.switch_user_button_content_description),
        resourceId = R.drawable.switch_arrows_24
    )

    fun getSettingsAction() = Action(
        title = resources.getString(R.string.widget_settings_button_content_description),
        contentDescription = resources.getString(R.string.widget_settings_button_content_description),
        resourceId = R.drawable.settings
    )

    suspend fun getItems() = listOf(getSwitchUserAction(), getAddUserAction())
}

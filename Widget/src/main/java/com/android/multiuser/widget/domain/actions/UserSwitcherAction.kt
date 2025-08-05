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

package com.android.multiuser.widget.domain.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.UsersRepository
import com.android.multiuser.widget.domain.OpenUserSwitcherUseCase
import com.android.multiuser.widget.MultiuserWidgetUtil

class UserSwitcherAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val userManager = MultiuserWidgetUtil.getUserManager(context)
        val activityManager = MultiuserWidgetUtil.getActivityManager(context)
        val usersRepo = UsersRepository(userManager,activityManager, context.contentResolver)
        val intentRepo = IntentRepository(context.resources)
        val interaction = OpenUserSwitcherUseCase(intentRepo, usersRepo)()
        interaction(context)
    }
}

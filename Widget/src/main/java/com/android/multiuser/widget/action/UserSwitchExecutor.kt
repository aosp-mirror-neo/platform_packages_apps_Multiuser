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

package com.android.multiuser.widget.action

import android.content.Context
import android.content.Intent
import android.os.UserHandle
import android.os.UserManager
import com.android.multiuser.widget.ui.DialogActivity
import com.android.multiuser.widget.MultiuserWidgetUtil

fun switchUsers(userId: Int, context: Context) {
    val userManager = MultiuserWidgetUtil.getUserManager(context)
    if(userManager == null) {
        showFailedDialog(context, /* showSwitchFailedDialog=*/ true)
        return
    }

    if(userManager.getUserSwitchability() != UserManager.SWITCHABILITY_STATUS_OK) {
        showFailedDialog(context, /* showSwitchFailedDialog=*/ true)
        return
    }

    if(!userManager.isUserSwitcherEnabled(true)) {
        showFailedDialog(context, /* showSwitchFailedDialog=*/ false)
        return
    }

    val activityManager = MultiuserWidgetUtil.getActivityManager(context)
    if(activityManager == null || !activityManager.switchUser(UserHandle(userId))) {
        showFailedDialog(context, /* showSwitchFailedDialog=*/ true)
    }
}

private fun showFailedDialog(context: Context, showSwitchFailedDialog: Boolean) {
    context.startActivity(
        Intent(context, DialogActivity::class.java).apply {
            putExtra("showFailedDialog", showSwitchFailedDialog)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

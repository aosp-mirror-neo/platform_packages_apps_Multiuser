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

import android.app.ActivityManager
import android.content.Context
import android.os.UserHandle
import android.os.UserManager

fun switchUsers(userId: Int, context: Context) {
    if(!canSwitchUser(context)) {
        // TODO let the user know switching is not allowed
        return
    }

    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    if(activityManager == null || !activityManager.switchUser(UserHandle(userId))) {
        // TODO let the user know switch can not be performed
    }
}

private fun canSwitchUser(context: Context): Boolean {
    val userManager = context.getSystemService(UserManager::class.java) as? UserManager
    if(userManager == null) {
        return false
    }
    return userManager.getUserSwitchability() == UserManager.SWITCHABILITY_STATUS_OK
            && userManager.isUserSwitcherEnabled(true)
}

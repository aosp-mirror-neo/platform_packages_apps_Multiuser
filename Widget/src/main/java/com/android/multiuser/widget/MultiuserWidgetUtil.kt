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

package com.android.multiuser.widget

import android.app.ActivityManager
import android.content.Context
import android.os.UserManager

object MultiuserWidgetUtil {
    private var userManager: UserManager? = null
    private var activityManager: ActivityManager? = null

    fun getUserManager(context: Context): UserManager? {
        if(userManager == null) {
            userManager = context
                .getApplicationContext()
                .getSystemService(UserManager::class.java) as? UserManager
        }

        return userManager
    }

    fun getActivityManager(context: Context): ActivityManager? {
        if(activityManager == null) {
            activityManager = context
                .getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        }

        return activityManager
    }
}

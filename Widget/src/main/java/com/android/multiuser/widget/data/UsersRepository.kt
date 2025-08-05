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

import android.app.ActivityManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ContentResolver
import android.content.pm.UserInfo
import android.multiuser.Flags
import android.os.UserHandle
import android.os.UserManager
import android.provider.Settings.Global
import kotlin.collections.List
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.data.model.UserAddRestriction
import com.android.multiuser.widget.data.model.UserSwitchRestrictions

class UsersRepository(private val userManager: UserManager?,
    private var activityManager: ActivityManager??,
    private val contentResolver: ContentResolver): IUsersRepository {
    val currentUserId = UserHandle.myUserId()

    override suspend fun getUsers(): List<User> {
        if (userManager == null) {
            // This ensures the error layout is shown in the UI.
            return listOf()
        }
        var userInfoList = userManager.getAliveUsers().filter { user ->
            if (Flags.widgetScalingBugfix()) {
                user.id == currentUserId
            } else {
                (user.isFull() && user.supportsSwitchTo())
            }
        }

        return userInfoList
            .map { userInfo: UserInfo ->
                User(
                    id = userInfo.id,
                    name = userInfo.name ?: "",
                    creationTime = userInfo.creationTime,
                    isCurrentUser = (currentUserId == userInfo.id),
                    isAdmin = userInfo.isAdmin(),
                    switchable = !disableSwitchUsers()
                )
            }
    }

    override suspend fun disableSwitchUsers(): Boolean {
        return checkUserSwitchRestrictions() != UserSwitchRestrictions.NONE
    }

    override fun isCurrentUserAdmin(): Boolean {
        return userManager?.isUserAdmin(currentUserId) ?: false
    }

    override fun checkUserAddRestrictions(widgetCategory: Int) : UserAddRestriction {
        val onLockscreen = widgetCategory == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD
        if(userManager == null) {
            return UserAddRestriction.UNKNOWN
        } else {
            if (!isCurrentUserAdmin()) {
                return UserAddRestriction.NOT_ADMIN_OR_SYSTEM
            }
            if (userManager.getUserSwitchability() != UserManager.SWITCHABILITY_STATUS_OK) {
                return UserAddRestriction.ONCALL_OR_LOCKED
            }
            if (userManager.hasUserRestriction(UserManager.DISALLOW_ADD_USER)) {
                return UserAddRestriction.WORK_POLICY
            }
            if (!userManager.isUserSwitcherEnabled(true)) {
                return UserAddRestriction.SWITCH_DISABLED
            }
            if (!userManager.canAddMoreUsers(UserManager.USER_TYPE_FULL_SECONDARY)) {
                return UserAddRestriction.MAX_USER_LIMIT
            }
            if(onLockscreen && !canAddUsersWhenLocked()) {
                return UserAddRestriction.DISABLED_ON_LOCKSCREEN
            }
        }
        return UserAddRestriction.NONE
    }

    override fun checkUserSwitchRestrictions(): UserSwitchRestrictions {
        if (userManager == null) {
            return UserSwitchRestrictions.UNKNOWN
        }
        if (userManager.hasUserRestriction(
                UserManager.DISALLOW_USER_SWITCH
            )
        ) {
            return UserSwitchRestrictions.WORK_POLICY
        }
        if (userManager.getUserSwitchability() !=
            UserManager.SWITCHABILITY_STATUS_OK
        ) {
            return UserSwitchRestrictions.ONCALL_OR_LOCKED
        }
        if (!userManager.isUserSwitcherEnabled(true)) {
            return UserSwitchRestrictions.DISABLED
        }
        return UserSwitchRestrictions.NONE
    }

    override fun switchToUser(userId: Int) :Boolean {
        return activityManager?.switchUser(UserHandle(userId))?: false
    }

    private fun canAddUsersWhenLocked(): Boolean {
        return Global.getInt(contentResolver, Global.ADD_USERS_WHEN_LOCKED, 0) != 0
    }
}

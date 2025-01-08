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

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.UserInfo
import android.graphics.Bitmap
import android.os.UserHandle
import android.os.UserManager
import com.android.multiuser.widget.data.model.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.List

class UsersRepository(private val userManager: UserManager?) {
    fun getUsers(): List<User> {
        if (userManager == null) {
            // This ensures the error layout is shown in the UI.
            return listOf()
        }
        var userInfoList = userManager.getAliveUsers()
            .filter { it.isFull() && it.supportsSwitchTo() }
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

    val currentUserId = UserHandle.myUserId()
    fun disableSwitchUsers() = userManager?.let {
        it.getUserSwitchability() != UserManager.SWITCHABILITY_STATUS_OK
                || !userManager.isUserSwitcherEnabled(true)
    } ?: true
    fun isCurrentUserAdmin() = userManager?.isUserAdmin(currentUserId) ?: false
}

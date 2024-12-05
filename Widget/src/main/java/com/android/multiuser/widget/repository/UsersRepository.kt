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

package com.android.multiuser.widget.repository

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.UserInfo
import android.graphics.Bitmap
import android.os.UserManager
import com.android.internal.util.UserIcons
import com.android.multiuser.widget.data.model.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.List

class UsersRepository(private val userManager: UserManager?) {
    fun getUsers(context: Context): List<User> {
        if(userManager == null) {
            // This ensures the error layout is shown in the UI.
            return listOf()
        }
        var userInfoList = userManager.getAliveUsers().filter { it.isFull() }
        return userInfoList.map { userInfo: UserInfo -> mapUserInfoToUser(userInfo, context) }
    }

    private fun mapUserInfoToUser(userInfo: UserInfo, context: Context): User {
        // Get the user icon. If the user manager doesn't provide an icon,
        // get the default icon and convert it to the appropriate size.
        val userIcon = userManager?.getUserIcon(userInfo.id) ?: UserIcons.getDefaultUserIcon(
            context.resources,
            userInfo.id,
            false
        ).let { UserIcons.convertToBitmapAtUserIconSize(context.resources, it) }


        return User(
            id = userInfo.id,
            name = userInfo.name ?: "",
            creationTime = userInfo.creationTime,
            iconPath = saveToInternalStorage(userIcon, userInfo.id, context),
            isCurrentUser = (context.user.identifier == userInfo.id),
            isAdmin = userManager?.isUserAdmin(userInfo.id) ?: false,
        )
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap, id: Int, context: Context): String {
        val cw = ContextWrapper(context)
        // path to /data/user/{id}/com.android.multiuser/app_imageDir
        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val filePath = File(directory, "$id.png")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(filePath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, /*quality= */100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return filePath.absolutePath
    }
}

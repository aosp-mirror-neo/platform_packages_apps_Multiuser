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
import android.graphics.Bitmap
import android.os.UserManager
import com.android.internal.util.UserIcons
import com.android.multiuser.widget.R

class ImageRepository(val userManager: UserManager?, val resources: Resources) {

suspend fun getAvatar(userId: Int): Bitmap = userManager?.getUserIcon(userId)
    ?: UserIcons.getDefaultUserIcon(resources, userId, false)
        .let { UserIcons.convertToBitmapAtUserIconSize(resources, it) }


    suspend fun getContentDescription(userName: String, isCurrent: Boolean): String {
        if (isCurrent) {
            return resources.getString(R.string.current_user_content_description, userName)
        }
        return userName
    }
}
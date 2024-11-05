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

package com.android.multiuser.widget.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserModels")
data class User(
        @PrimaryKey val id: Int,
        val name: String,
        val creationTime: Long,
        val iconPath: String,
        var isCurrentUser: Boolean,
        val isAdmin: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return id == other.id &&
            name == other.name &&
            creationTime == other.creationTime &&
            iconPath == other.iconPath &&
            isCurrentUser == other.isCurrentUser &&
            isAdmin == other.isAdmin
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + creationTime.hashCode()
        result = 31 * result + iconPath.hashCode()
        result = 31 * result + isCurrentUser.hashCode()
        result = 31 * result + isAdmin.hashCode()
        return result
    }
}
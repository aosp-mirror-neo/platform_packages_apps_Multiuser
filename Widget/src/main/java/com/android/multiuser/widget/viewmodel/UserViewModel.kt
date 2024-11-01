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

package com.android.multiuser.widget.viewmodel

data class UserViewModel(
    val iconPath: String,
    val id: Int,
    val name: String,
    override val contentDescription: String?,
    val isSelected: Boolean
) : GridItemViewModel(name, contentDescription) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserViewModel

        return iconPath == other.iconPath &&
            id == other.id &&
            name == other.name &&
            contentDescription == other.contentDescription &&
            isSelected == other.isSelected
    }

    override fun hashCode(): Int {
        var result = iconPath.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + contentDescription.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}

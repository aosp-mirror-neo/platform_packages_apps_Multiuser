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

package com.android.multiuser.widget.viewmodel

import com.android.multiuser.widget.viewmodel.ButtonState.Flag

class ButtonState(var flag: Int){

    constructor(enabled: Boolean, visible: Boolean)
            : this(enabled.toFlagInt(Flag.Enabled) + visible.toFlagInt(Flag.Visible))

    enum class Flag(val value: Int) {
        Enabled(1), // 00000001
        Visible(2)  // 00000010
    }

    fun setEnabled(enabled: Boolean)  = updateFlag(Flag.Enabled, enabled)
    fun isEnabled() = (flag and Flag.Enabled.value) > 0
    fun setVisible(visible: Boolean)  = updateFlag(Flag.Visible, visible)
    fun isVisible() = (flag and Flag.Visible.value) > 0

    private fun updateFlag(newFlag: Flag, enabled: Boolean) {
        if (enabled)
            flag = flag or newFlag.value
        else
            flag = flag and newFlag.value.inv()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ButtonState

        return flag == other.flag
    }

    override fun hashCode(): Int {
        return flag.hashCode()
    }
}

fun Boolean.toFlagInt(flag: Flag) = if (this) flag.value else 0

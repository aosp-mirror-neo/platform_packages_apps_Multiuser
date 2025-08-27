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

import android.graphics.Bitmap
import androidx.glance.action.Action
import com.android.multiuser.widget.domain.restrictions.IStateUseCase
import com.android.multiuser.widget.domain.LoadUserAvatarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel(
    val id: Int,
    val name: String,
    contentDescription: String?,
    val isSelected: Boolean,
    private val loadAvatarUseCase: LoadUserAvatarUseCase,
    action: Action,
    stateUseCase: IStateUseCase<ButtonState>
) : GridItemViewModel(key="user_$id", name, contentDescription, action, stateUseCase) {
    private val _uiState = MutableStateFlow<UiState>(UiState.Success)
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val bitmap: StateFlow<Bitmap?> = _bitmap.asStateFlow()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserViewModel

        return bitmap.value == other.bitmap.value &&
                id == other.id &&
                name == other.name &&
                contentDescription == other.contentDescription &&
                isSelected == other.isSelected &&
                state == other.state
    }

    override fun hashCode(): Int {
        var result = bitmap.value.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + contentDescription.hashCode()
        result = 31 * result + isSelected.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }

    suspend fun loadAvatar() {
        _uiState.value = UiState.Loading
        _bitmap.value = loadAvatarUseCase()
        _uiState.value = UiState.Success
    }

    fun invalidate() {
        _uiState.value = UiState.Invalid
    }
}

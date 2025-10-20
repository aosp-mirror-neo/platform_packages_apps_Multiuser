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

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.action.Action
import androidx.glance.action.LambdaAction
import com.android.multiuser.widget.domain.restrictions.DefaultStateUseCase
import com.android.multiuser.widget.domain.restrictions.IStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class GridItemViewModel(
    val key: String,
    val title: String,
    val contentDescription: String? = null,
    val action: Action = LambdaAction("GridItemViewModel $key", {}),
    val stateUseCase: IStateUseCase<ButtonState> = DefaultStateUseCase()
) {
    private val _state: MutableStateFlow<ButtonState> = MutableStateFlow(ButtonState(true, true))
    val state = _state.asStateFlow()

    // Always refresh ViewModel before use
    open suspend fun refresh(prefs: Preferences?) {
        val stateFlags = prefs?.get(intPreferencesKey(key))
        val newState = stateUseCase()
        if(stateFlags == null || ButtonState(stateFlags) != newState) {
            setState(newState)
        } else {
            setState(ButtonState(stateFlags))
        }
    }

   private fun setState(newState: ButtonState) {
        if (newState.flag != state.value.flag) {
            _state.value = newState
        }
    }
}

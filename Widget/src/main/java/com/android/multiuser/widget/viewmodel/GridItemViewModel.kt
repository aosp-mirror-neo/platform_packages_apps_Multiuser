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

import androidx.glance.action.Action
import androidx.glance.action.LambdaAction
import com.android.multiuser.widget.domain.restrictions.DefaultStateUseCase
import com.android.multiuser.widget.domain.restrictions.IStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class GridItemViewModel(
    val title: String,
    val contentDescription: String? = null,
    val action: Action = LambdaAction("GridItemViewModel $title", {}),
    val enabledUseCase: IStateUseCase<Boolean> = DefaultStateUseCase()
) {
    private val _enabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val enabled = _enabled.asStateFlow()

    // Always refresh ViewModel before use
    open suspend fun refresh() {
        setEnabled(enabledUseCase())
    }

   private fun setEnabled(newState: Boolean) {
        if (newState != enabled.value) {
            _enabled.value = newState
        }
    }
}

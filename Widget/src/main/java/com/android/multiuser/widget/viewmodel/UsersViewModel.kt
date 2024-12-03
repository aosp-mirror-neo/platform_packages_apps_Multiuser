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

import android.util.Log
import com.android.multiuser.widget.domain.ILoadUsersUseCase
import java.lang.Exception
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersViewModel(private val useCase: ILoadUsersUseCase) {
    val data: StateFlow<List<GridItemViewModel>> = getData()
    val uiState: StateFlow<UiState> = getState()

    companion object {
        private val _data = MutableStateFlow<List<GridItemViewModel>>(emptyList())
        private val _uiState = MutableStateFlow<UiState>(UiState.Invalid)

        fun setState(value: UiState) {
            if(_uiState.value != value) {
                _uiState.value = value
            }
        }
        fun getData() = _data.asStateFlow()
        fun setData(value: List<GridItemViewModel>) {
            if (value != _data.value) {
                _data.value = value
            }
        }

        fun getState() = _uiState.asStateFlow()

        fun invalidate() {
            setState(UiState.Invalid)
        }
    }

    suspend fun reload() {
        if (uiState.value != UiState.Loading) {
            setState(UiState.Loading)
            try {
                val newData = useCase()
                if (newData != data.value) {
                    setData(newData)
                }
                if (data.value.size == 0) {
                    setState(UiState.Error)
                } else {
                    setState(UiState.Success)
                }

            } catch (exc: Exception) {
                Log.e(this.javaClass.name, "Unspecified exception")
                setState(UiState.Error)
            }
        }
    }

}

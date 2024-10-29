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

package com.android.multiuser.widget.domain

import com.android.multiuser.widget.data.UserDao
import com.android.multiuser.widget.data.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Maps data from the repository to the local Room database.
 */
class UpdateUsersUseCase(
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend fun onUserNameChanged(user: User): Flow<Boolean> = withContext(dispatcher) {
        return@withContext updateUser(user)
    }

    suspend fun onUserAvatarChanged(user: User): Flow<Boolean> = withContext(dispatcher) {
        return@withContext updateUser(user)
    }

    suspend fun onUserIsAdminChanged(user: User): Flow<Boolean> = withContext(dispatcher) {
        return@withContext updateUser(user)
    }

    suspend fun onUserAdded(user: User): Flow<Boolean> = withContext(dispatcher) {
        return@withContext flow {
            userDao.addUser(user)
            emit(true)
        }
    }

    suspend fun onUserRemoved(user: User): Flow<Boolean> = withContext(dispatcher) {
        return@withContext flow {
            userDao.deleteUser(user)
            emit(true)
        }
    }

    suspend fun onUserSwitch(newCurrentUser: User): Flow<Boolean> = withContext(dispatcher) {
        return@withContext flow {
            var previousCurrentUser = userDao.getCurrentUser().first()
            previousCurrentUser.isCurrentUser = false

            userDao.updateUsers(previousCurrentUser, newCurrentUser)
        }
    }

    private fun updateUser(user: User): Flow<Boolean> {
        return flow {
            userDao.updateUsers(user)
            emit(true)
        }
    }
}

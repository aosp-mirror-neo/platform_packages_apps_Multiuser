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

import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.UserDao
import com.android.multiuser.widget.data.model.Action
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.util.SHOULD_DISPLAY_ADD_USER_BUTTON
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.UsersViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 * Maps data from the local Room database into viewmodel [UsersViewModel].
 */
class LoadUsersUseCase(
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val actionsRepository: ActionsRepository
) {
    suspend operator fun invoke(): Flow<UsersViewModel> = withContext(dispatcher) {
        return@withContext loadUsersViewModel()
    }

    private fun loadUsersViewModel(): Flow<UsersViewModel>  {
        return flow {
            val model = UsersViewModel(
                users = getUserViewModels(),
                actions = getActionViewModels(),
            )
            emit(model)
        }
    }

    private suspend fun getUserViewModels() =
            userDao.getUsers().first().map { user -> mapUserToUserViewModel(user) }

    private suspend fun getActionViewModels(): List<ActionViewModel> {
        // If the current user is an admin and SHOW_ADD_USER_BUTTON is set to true, include the
        // button for adding users.
        if(userDao.getCurrentUser().first().isAdmin && SHOULD_DISPLAY_ADD_USER_BUTTON) {
            return listOf(mapActionToActionViewModel(actionsRepository.getAddUserAction()))
        }

        return emptyList()
    }

    private fun mapUserToUserViewModel(user: User): UserViewModel {
        return UserViewModel(
            iconPath = user.iconPath,
            id = user.id,
            name = user.name,
            isSelected = user.isCurrentUser,
            // contentDescription is auto-generated because user icons are selectable
            contentDescription = null,
        )
    }

    private fun mapActionToActionViewModel(action: Action): ActionViewModel {
        return ActionViewModel (
            title = action.title,
            resourceId = action.resourceId,
            contentDescription = action.contentDescription
        )
    }
}

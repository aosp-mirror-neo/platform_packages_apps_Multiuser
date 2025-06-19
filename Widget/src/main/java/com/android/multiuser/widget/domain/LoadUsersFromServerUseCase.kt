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

import android.multiuser.Flags
import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.ImageRepository
import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.IUsersRepository
import com.android.multiuser.widget.data.model.Action
import com.android.multiuser.widget.util.SHOULD_DISPLAY_ADD_USER_BUTTON
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.GridItemViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.List

/**
 * Maps data from the local service into list of GridItemViewModel.
 */
class LoadUsersFromServerUseCase(
    private val usersRepository: IUsersRepository,
    private val actionsRepository: ActionsRepository,
    private val imageRepository: ImageRepository,
    private val intentRepository: IntentRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILoadUsersUseCase{
    override suspend operator fun invoke(): List<GridItemViewModel> = withContext(dispatcher) {
        return@withContext getUserViewModels() + getActionViewModels()
    }
    private suspend fun getUserViewModels() =
        usersRepository.getUsers().map { user -> UserViewModel (
            id = user.id,
            name = user.name,
            isSelected = user.isCurrentUser,
            // TODO: check content auto-generated description.
            // contentDescription is auto-generated because user icons are selectable
            contentDescription = null,
            enabled = user.isCurrentUser || user.switchable,
            loadAvatarUseCase = LoadUserAvatarUseCase(user.id, imageRepository),
            userSwitchUseCase = UserSwitchUseCase(user.id, usersRepository, intentRepository)
        )}

    private suspend fun getActionViewModels(): List<ActionViewModel> {
        // If the current user is an admin and SHOW_ADD_USER_BUTTON is set to true, include the
        // button for adding users.
        if (Flags.widgetCurrentUserView() && usersRepository.isCurrentUserAdmin()) {
            return actionsRepository.getItems().map{ mapActionToActionViewModel(it)}
        }
        if(usersRepository.isCurrentUserAdmin() && SHOULD_DISPLAY_ADD_USER_BUTTON) {
            return listOf(mapActionToActionViewModel(actionsRepository.getAddUserAction()))
        }

        return emptyList()
    }

    private fun mapActionToActionViewModel(action: Action): ActionViewModel {
        return ActionViewModel (
            title = action.title,
            resourceId = action.resourceId,
            contentDescription = action.contentDescription
        )
    }
}
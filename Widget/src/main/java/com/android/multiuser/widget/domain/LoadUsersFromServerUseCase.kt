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
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback

import com.android.multiuser.widget.domain.actions.UserSwitcherAction
import com.android.multiuser.widget.domain.actions.SwitchUserAction
import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.ImageRepository
import com.android.multiuser.widget.data.IntentRepository
import com.android.multiuser.widget.data.IUsersRepository
import com.android.multiuser.widget.data.model.UserAddRestriction
import com.android.multiuser.widget.domain.actions.AddUserAction
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
    private val widgetCategory: Int,
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
            contentDescription = imageRepository.getContentDescription(
                user.name,
                user.isCurrentUser
            ),
            enabled = user.isCurrentUser || user.switchable,
            loadAvatarUseCase = LoadUserAvatarUseCase(user.id, imageRepository),
            action = actionRunCallback<SwitchUserAction>(
                parameters = actionParametersOf(SwitchUserAction.userIdKey to user.id)
            )
        )}

    private suspend fun getActionViewModels(): List<ActionViewModel> {
        if (Flags.widgetScalingBugfix()) {
            if (usersRepository.checkUserAddRestrictions(widgetCategory) == UserAddRestriction.NONE) {
                return listOf(
                    actionsRepository.getSwitchUserAction()
                        .toViewModel(actionRunCallback<UserSwitcherAction>()),
                    actionsRepository.getAddUserAction()
                        .toViewModel(actionRunCallback<AddUserAction>(
                            parameters = actionParametersOf(AddUserAction.widgetCategory to widgetCategory)
                        ))
                )
            } else {
                return listOf(
                    actionsRepository.getSwitchUserAction()
                        .toViewModel(actionRunCallback<UserSwitcherAction>())
                )
            }
        }
        // If the current user is an admin and SHOW_ADD_USER_BUTTON is set to true, include the
        // button for adding users.
        if(usersRepository.isCurrentUserAdmin() && SHOULD_DISPLAY_ADD_USER_BUTTON) {
            return listOf(
                actionsRepository.getAddUserAction().toViewModel(
                    actionRunCallback<AddUserAction>(
                        parameters = actionParametersOf(AddUserAction.widgetCategory  to widgetCategory)
                    )
                )
            )
        }
        return emptyList()
    }
}
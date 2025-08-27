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
package com.android.multiuser.widget.ui.tests


import android.appwidget.AppWidgetProviderInfo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.ImageRepository
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.data.model.UserAddRestriction
import com.android.multiuser.widget.data.model.UserSwitchRestrictions
import com.android.multiuser.widget.domain.LoadUsersFromServerUseCase
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.R

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class LoadUsersFromServerUseCaseTest {

    val usersRepository = FakeUserRepository()
    val widgetCategory = 1
    val  context = InstrumentationRegistry.getInstrumentation().context
    lateinit var actionsRepository: ActionsRepository
    lateinit var imageRepository: ImageRepository
    val addUserTitle = context.resources.getString(R.string.add_user_button_title)
    val switchTitle = context.resources.getString(R.string.switch_user_button_title)


    @Before
    fun setup() {
        usersRepository.users = listOf<User>( User(  10, "main",  0,  true,  true, true ))
        actionsRepository = ActionsRepository(context.resources)
        imageRepository = ImageRepository(null, context.resources)
    }



    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun allButtonsEnabledWhenNoRestrictions() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.NONE
        usersRepository.userRestrictions = UserSwitchRestrictions.NONE
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)
        actions.forEach{ action:ActionViewModel ->
            assertTrue(action.enabled.value)
        }
    }



    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun allButtonsDisabledWhenSwitchDisabled() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.NONE
        usersRepository.userRestrictions = UserSwitchRestrictions.DISABLED
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)
        actions.forEach{ action:ActionViewModel ->
            assertFalse(action.enabled.value)
        }
    }



    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun allButtonsDisabledDuringTheCall() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.ONCALL_OR_LOCKED
        usersRepository.userRestrictions = UserSwitchRestrictions.ONCALL_OR_LOCKED
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)
        actions.forEach{ action:ActionViewModel ->
            assertFalse (action.enabled.value)
        }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun allButtonsDisabledForWorkPolicy() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.WORK_POLICY
        usersRepository.userRestrictions = UserSwitchRestrictions.WORK_POLICY
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)
        actions.forEach{ action:ActionViewModel ->
            assertFalse (action.enabled.value)
        }
    }



    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addUserNotVisibleWhenNotAnAdmin() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.NOT_ADMIN_OR_SYSTEM
        usersRepository.userRestrictions = UserSwitchRestrictions.NONE
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(2, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(1, actions.size)

        val switchUser = actions.firstOrNull{action -> action.title == switchTitle}
        assertNotNull(switchUser)
        assertTrue(switchUser!!.enabled.value)
    }


    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addUserDisabledWhenLimitReached() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.MAX_USER_LIMIT
        usersRepository.userRestrictions = UserSwitchRestrictions.NONE
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)

        val addUser = actions.firstOrNull{action -> action.title == addUserTitle}
        assertNotNull(addUser)
        assertFalse(addUser!!.enabled.value)

        val switchUser = actions.firstOrNull{action -> action.title == switchTitle}
        assertNotNull(switchUser)
        assertTrue(switchUser!!.enabled.value)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addUserEnabledWhenUserUnlocked() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.DISABLED_ON_LOCKSCREEN
        usersRepository.userRestrictions = UserSwitchRestrictions.NONE
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)

        val addUser = actions.firstOrNull{action -> action.title == addUserTitle}
        assertNotNull(addUser)
        assertTrue(addUser!!.enabled.value)

        val switchUser = actions.firstOrNull{action -> action.title == switchTitle}
        assertNotNull(switchUser)
        assertTrue(switchUser!!.enabled.value)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addUserDisabledOnLockScreen() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.DISABLED_ON_LOCKSCREEN
        usersRepository.userRestrictions = UserSwitchRestrictions.NONE
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)

        val addUser = actions.firstOrNull{action -> action.title == addUserTitle}
        assertNotNull(addUser)
        assertFalse(addUser!!.enabled.value)

        val switchUser = actions.firstOrNull{action -> action.title == switchTitle}
        assertNotNull(switchUser)
        assertTrue(switchUser!!.enabled.value)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun addUserEnabledOnLockScreen() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.NONE
        usersRepository.userRestrictions = UserSwitchRestrictions.NONE
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)

        val addUser = actions.firstOrNull{action -> action.title == addUserTitle}
        assertNotNull(addUser)
        assertTrue(addUser!!.enabled.value)

        val switchUser = actions.firstOrNull{action -> action.title == switchTitle}
        assertNotNull(switchUser)
        assertTrue(switchUser!!.enabled.value)
    }

    /* Add user is disabled whenever switch is disabled.
     * Adding new user automatically switch to that user just after creation */

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun allButtonsDisabledWhenSwitchOnCall() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.NONE
        usersRepository.userRestrictions = UserSwitchRestrictions.ONCALL_OR_LOCKED
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)
        actions.forEach{ action:ActionViewModel ->
            assertFalse(action.enabled.value)
        }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun allButtonsDisabledWhenSwitchWorkPolicy() = runBlocking {
        // Arrange
        usersRepository.addUserRestrictions = UserAddRestriction.NONE
        usersRepository.userRestrictions = UserSwitchRestrictions.WORK_POLICY
        val useCase =
            LoadUsersFromServerUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )

        val result = useCase()
        // Assert
        assertNotNull(result)
        assertEquals(3, result.size)
        val users: List<UserViewModel> = result.filterIsInstance<UserViewModel>()
        assertEquals(1, users.size)
        val actions: List<ActionViewModel> = result.filterIsInstance<ActionViewModel>()
        assertEquals(2, actions.size)
        actions.forEach{ action:ActionViewModel ->
            assertFalse(action.enabled.value)
        }
    }
}

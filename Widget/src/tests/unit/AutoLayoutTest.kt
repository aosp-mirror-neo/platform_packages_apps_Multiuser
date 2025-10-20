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


import android.content.Context
import android.content.res.Resources
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.ImageRepository
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.domain.LoadCurrentUserUseCase
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import com.android.multiuser.widget.viewmodel.LayoutViewModel
import com.android.multiuser.widget.viewmodel.stacks.LayoutStacks

import kotlin.test.assertEquals
import kotlin.test.assertNotNull

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class AutoLayoutTest {

    val usersRepository = FakeUserRepository()
    val widgetCategory = 0
    val context: Context = InstrumentationRegistry.getInstrumentation().context
    val resources: Resources = context.resources
    lateinit var actionsRepository: ActionsRepository
    lateinit var imageRepository: ImageRepository


    @Before
    fun setup() {
        usersRepository.users = listOf<User>(User(10, "main", 0, true, true, true))
        actionsRepository = ActionsRepository(resources)
        imageRepository = ImageRepository(null, resources)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testTallWide() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testTallWide")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(279.dp, 210.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.tallWide().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testWide() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testWide")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(306.dp, 152.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.wide().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testDefault() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testDefault")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(190.dp, 185.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.default().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testShortWide() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testShortWide")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(306.dp, 104.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.shortWide().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testShort() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testShort")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(220.dp, 104.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.short().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testShortSlim() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testShortSlim")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(164.dp, 104.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.shortSlim().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testTallLarge() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testTallLarge")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(167.dp, 334.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.tallLarge().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testTall() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testTall")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(165.dp, 246.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.tall().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testTallSlim() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testTallSlim")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(128.dp, 246.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.tallSlim().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testSlim() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testSlim")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(128.dp, 168.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.slim().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testTallerSlim() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testTallerSlim")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(128.dp, 274.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.tallerSlim().TAG, layout.arrangement.TAG)
    }

    @Test
    fun testTaller() = runBlocking {
        val useCase =
            LoadCurrentUserUseCase(
                usersRepository,
                actionsRepository,
                imageRepository,
                widgetCategory
            )
        val items = useCase()
        val userViewModel = items.filterIsInstance<UserViewModel>().firstOrNull() as? UserViewModel
        val actionViewModelList = items.filterIsInstance<ActionViewModel>()

        assertNotNull(userViewModel, "UserViewModel should not be null for testTaller")

        val layouts = LayoutStacks(resources, userViewModel, actionViewModelList)
        val layout = LayoutViewModel(DpSize(165.dp, 350.dp), resources, userViewModel, actionViewModelList)
        assertEquals(layouts.taller().TAG, layout.arrangement.TAG)
    }
}
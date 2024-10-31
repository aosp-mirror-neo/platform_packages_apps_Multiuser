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

package com.android.multiuser.widget.tests.unit.domain

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.multiuser.widget.tests.R
import com.android.multiuser.widget.data.ActionsRepository
import com.android.multiuser.widget.data.UserDao
import com.android.multiuser.widget.data.UsersDatabase
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.domain.LoadUsersUseCase
import com.android.multiuser.widget.viewmodel.ActionViewModel
import com.android.multiuser.widget.viewmodel.UserViewModel
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadUsersUseCaseTest {
    private lateinit var database: UsersDatabase
    private lateinit var userDao: UserDao
    private lateinit var loadUsersUseCase: LoadUsersUseCase
    private lateinit var actionsRepository: ActionsRepository
    private val addUserActionViewModel = ActionViewModel (
        resourceId = R.drawable.person_add,
        title = "Add user",
        contentDescription = "Add user",
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        actionsRepository = ActionsRepository(context)
        database = Room.inMemoryDatabaseBuilder(context, UsersDatabase::class.java).build()
        userDao = database.getUserDao()
        loadUsersUseCase =
            LoadUsersUseCase(userDao = userDao, actionsRepository = actionsRepository)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun loadUsersModel_mapsUserToUserViewModel() = runBlocking {
        val fakeUser1 = User(
            id = 0,
            name = "fake_user_name_1",
            creationTime = 0,
            iconPath = "fake_icon_path_1",
            isCurrentUser = true,
            isAdmin = false,
        )
        val fakeUser2 = User(
            id = 1,
            name = "fake_user_name_2",
            creationTime = 1,
            iconPath = "fake_icon_path_2",
            isCurrentUser = false,
            isAdmin = true,
        )
        userDao.addUsers(fakeUser1)
        userDao.addUsers(fakeUser2)

        val expectedUserViewModel1 = UserViewModel(
            iconPath = "fake_icon_path_1",
            id = 0,
            name = "fake_user_name_1",
            contentDescription = null,
            isSelected = true,
        )
        val expectedUserViewModel2 = UserViewModel(
            iconPath = "fake_icon_path_2",
            id = 1,
            name = "fake_user_name_2",
            contentDescription = null,
            isSelected = false,
        )

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val model = loadUsersUseCase().first()
            assertEquals(model.users.size, 2)
            assertContains(model.users, expectedUserViewModel1)
            assertContains(model.users, expectedUserViewModel2)
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun currentUserIsAdmin_viewmodelIncludesAddUserButton() = runBlocking {
        val fakeUser = User(
            id = 0,
            name = "fake_user_name",
            creationTime = 0,
            iconPath = "fake_icon_path",
            isCurrentUser = true,
            isAdmin = true,
        )
        userDao.addUsers(fakeUser)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val model = loadUsersUseCase().first()
            assertEquals(model.actions.size, 1)
            assertContains(model.actions, addUserActionViewModel)
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun currentUserIsNotAdmin_viewmodelDoesNotIncludeAddUserButton() = runBlocking {
        val fakeUser = User(
            id = 0,
            name = "fake_user_name",
            creationTime = 0,
            iconPath = "fake_icon_path",
            isCurrentUser = true,
            isAdmin = false,
        )
        userDao.addUsers(fakeUser)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val model = loadUsersUseCase().first()
            assertFalse(model.actions.contains(addUserActionViewModel))
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }
}

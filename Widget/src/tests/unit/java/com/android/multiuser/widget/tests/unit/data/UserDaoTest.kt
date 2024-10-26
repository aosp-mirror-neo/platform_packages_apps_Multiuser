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

package com.android.multiuser.widget.tests.unit.data

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.data.UserDao
import com.android.multiuser.widget.data.UsersDatabase
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var database: UsersDatabase
    private lateinit var UserDao: UserDao

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, UsersDatabase::class.java).build()
        UserDao = database.getUserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun addUser_returnsTrue() = runBlocking {
        val fakeUser = User(
            id = 0,
            name = "fake_user_name",
            creationTime = 0,
            iconUri = "fake_icon_uri",
            isCurrentUser = false,
            isAdmin = false,
        )
        UserDao.addUser(fakeUser)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val UsersFromDatabase = UserDao.getUsers().first()
            assertEquals(UsersFromDatabase.size, 1)
            assertEquals(UsersFromDatabase.get(0), fakeUser)
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun addUsers_returnsOrderedUserInfos() = runBlocking {
        val fakeUser0 = User(
            id = 0,
            name = "fake_user_name_0",
            creationTime = 0,
            iconUri = "fake_icon_uri_0",
            isCurrentUser = false,
            isAdmin = false,
        )
        val fakeUser1 = User(
            id = 1,
            name = "fake_user_name_1",
            creationTime = 1,
            iconUri = "fake_icon_uri_1",
            isCurrentUser = false,
            isAdmin = false,
        )

        UserDao.addUser(fakeUser1)
        UserDao.addUser(fakeUser0)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val UsersFromDatabase = UserDao.getUsers().first()
            assertEquals(UsersFromDatabase.size, 2)
            assertTrue(UsersFromDatabase.get(0).creationTime
                    <= UsersFromDatabase.get(1).creationTime)
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun addUsers_returnsCurrentUser() = runBlocking {
        val fakeCurrentUser = User(
            id = 0,
            name = "fake_user_name_0",
            creationTime = 0,
            iconUri = "fake_icon_uri_0",
            isCurrentUser = true,
            isAdmin = false,
        )
        val fakeUser = User(
            id = 1,
            name = "fake_user_name_1",
            creationTime = 1,
            iconUri = "fake_icon_uri_1",
            isCurrentUser = false,
            isAdmin = false,
        )

        UserDao.addUser(fakeCurrentUser)
        UserDao.addUser(fakeUser)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val currentUser = UserDao.getCurrentUser().first()
            assertEquals(currentUser, fakeCurrentUser)
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun updateUsers_replacesExistingUserInfoWithNew() = runBlocking {
        val fakeUser = User(
            id = 0,
            name = "fake_user_name",
            creationTime = 0,
            iconUri = "fake_icon_uri",
            isCurrentUser = false,
            isAdmin = false,
        )
        val updatedFakeUser = User(
            id = 0,
            name = "updated_fake_user_name",
            creationTime = 0,
            iconUri = "updated_fake_icon_uri",
            isCurrentUser = false,
            isAdmin = false,
        )
        UserDao.addUser(fakeUser)
        UserDao.addUser(updatedFakeUser)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            val UsersFromDatabase = UserDao.getUsers().first()
            assertEquals(UsersFromDatabase.size, 1)
            assertEquals(UsersFromDatabase.get(0), updatedFakeUser)
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()
    }
}

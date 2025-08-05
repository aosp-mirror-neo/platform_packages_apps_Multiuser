package com.android.multiuser.widget.ui.tests
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

import com.android.multiuser.widget.data.IUsersRepository
import com.android.multiuser.widget.data.model.User
import com.android.multiuser.widget.data.model.UserAddRestriction
import com.android.multiuser.widget.data.model.UserSwitchRestrictions
import com.android.multiuser.widget.R

import kotlin.collections.List

/**
 * Fake User Repository so we don't have to mock UserManager
 */
class FakeUserRepository : IUsersRepository {
    var users = listOf<User>( User(  10, "main",  0,  true,  true, true ),
        User(  11, "secondary",  1,  false,  true, true ))
    var currentUserAdmin = true
    var addUserRestrictions = UserAddRestriction.NONE
    var userRestrictions = UserSwitchRestrictions.NONE
    var switchSucceeded = true

    override suspend fun getUsers() = users
    override suspend fun disableSwitchUsers() = userRestrictions != UserSwitchRestrictions.NONE
    override fun isCurrentUserAdmin() = currentUserAdmin
    override fun checkUserSwitchRestrictions() = userRestrictions
    override fun checkUserAddRestrictions(widgetCategory: Int) = addUserRestrictions
    override fun switchToUser(userId: Int) = switchSucceeded
}
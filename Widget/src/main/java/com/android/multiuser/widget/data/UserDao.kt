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

package com.android.multiuser.widget.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.android.multiuser.widget.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM UserModels ORDER BY creationTime ASC")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM UserModels WHERE isCurrentUser = TRUE LIMIT 1")
    fun getCurrentUser(): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(vararg users: User)

    @Update
    fun updateUsers(vararg users: User)

    @Delete
    fun deleteUser(vararg users: User)
}
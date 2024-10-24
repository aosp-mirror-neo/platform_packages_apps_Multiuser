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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.multiuser.widget.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {

    // This is how the database exposes DAOs.
    abstract fun getUserDao(): UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                        UsersDatabase::class.java,
                    "multiuser_widget_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}